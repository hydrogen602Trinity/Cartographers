import '@testing-library/jest-dom';
import { fireEvent, queryByText, render, renderHook, waitFor } from '@testing-library/react';
import App from 'App';
import fetch from 'jest-fetch-mock';
import { useEffect } from 'react';
import { MinecraftMap } from 'utilities/api';

fetch.enableMocks();

const m: MinecraftMap = {
    id: 1,
    name: "a name",
    upload_date: -10,
    author: "an author",
    length: "1 meter",
    objective_main: 1,
    objective_bonus: 2,
    difficulty: "yes",
    description_short: "a map",
    download_count: 42,
    type: "example",
    image_url: "example.png",
    series: "the test case series",
    mc_version: "1.0.0"
};

beforeEach(() => {
    fetch.resetMocks();
    process.env.REACT_APP_PRODUCTION_API = "http://localhost:8080";
    process.env.REACT_APP_DEVELOPMENT_API = "http://localhost:8080";
    fetchMock.mockIf(/localhost:8080/, req => Promise.resolve((e => {
        const url = new URL(req.url);
        switch (url.pathname) {
            case '/search/maps':
                return JSON.stringify([m]);
            case '/maps/count':
                return '1'
            case '/maps/1':
                return JSON.stringify(m);
            default:
                return {
                    status: 404,
                    body: 'Not found'
                };
        }
    })()));
});

it('CI test', () => {
    expect(42).toEqual(42);
});

/**
 * Ensures the App renders and the logo is defined
 */
test('rendering & logo', async () => {
    let out = render(<App></App>);
    let logo = await waitFor(() => out.getByAltText('CTM Repository'));
    await waitFor(() => out.getByTestId('home-map-display')); // to wait until things load
    expect(logo).toBeInTheDocument();
    expect(logo).toBeVisible();
    // @ts-ignore
    expect(logo.src).toBe("http://localhost/logo.webp");
});

/**
 * Ensures that the important parts of a map are displayed in the home view
 */
test('rendering & loading maps', async () => {
    let out = render(<App></App>);
    let parts = await waitFor(() => out.getByTestId('home-map-display'));
    let images = out.getAllByAltText('Map Image');
    expect(images.length).toBe(1);
    // @ts-ignore
    expect(images[0].src).toBe("http://localhost/example.png");
    expect(parts).toBeVisible();
    expect(parts.getElementsByTagName('img')[0]).toBeVisible();
    expect(out.getByText("an author")).toBeVisible();
    expect(out.getByText("a name")).toBeVisible();
    expect(out.getByText("1.0.0")).toBeVisible();
});

/**
 * Ensures that the page navigation looks like it should
 */
test('rendering & pages nav', async () => {
    let out = render(<App></App>);
    let parts = await waitFor(() => out.getByTestId('home-map-display'));
    let nav = out.getByLabelText('pagination navigation');
    expect(nav).toBeVisible();
    expect(queryByText(nav, '1')).toBeVisible();
    // only one page -> only one map
    expect(queryByText(nav, '2')).toBeFalsy();

    // one page, so both back and forth should be disabled
    let prev = out.getByLabelText('Go to previous page');
    expect(prev).toBeVisible();
    expect(prev).toBeDisabled();

    let next = out.getByLabelText('Go to next page');
    expect(next).toBeVisible();
    expect(next).toBeDisabled();
});

/**
 * Tests whether its possible to navigate to a map and back to the home menu
 */
test('navigation to map', async () => {
    let out = render(<App></App>);
    let parts = await waitFor(() => out.getByTestId('home-map-display'));

    let map = out.getByLabelText('map card');

    renderHook(() => {
        useEffect(() => map.click(), []);
    })

    let panel = await out.findByLabelText('Home');

    expect(panel).toBeVisible();
    expect(out.queryByText('a name')).toBeTruthy();
    expect(out.queryByText('an author')).toBeTruthy();
    let img = out.getByAltText('Map Image');
    // @ts-ignore
    expect(img.src).toBe('http://localhost/example.png');

    expect(out.queryByLabelText('map card')).toBeNull();
    expect(out.queryByTestId('home-map-display')).toBeNull();
    expect(out.queryByLabelText('map card')).toBeNull();

    renderHook(() => {
        useEffect(() => panel.click(), []);
    });

    let map2 = await out.findByLabelText('map card');

    expect(out.queryByLabelText('map card')).toBeTruthy();
    expect(out.queryByTestId('home-map-display')).toBeTruthy();
    expect(out.queryByLabelText('map card')).toBeTruthy();
    expect(out.queryByLabelText('Home')).toBeNull();
});

/**
 * Tests the search bar
 */
test('rendering & pages nav', async () => {
    let out = render(<App></App>);
    let parts = await waitFor(() => out.getByTestId('home-map-display'));

    expect(fetch.mock.calls.length).toBe(2);
    expect(fetch.mock.calls[0][0]).toBe('http://localhost:8080/maps/count');
    expect(fetch.mock.calls[1][0]).toBe('http://localhost:8080/search/maps?q=&per_page=10&page=1');

    let search = out.getByLabelText('search term');

    fireEvent.change(search, { target: { value: 'some&info' } });

    // @ts-ignore
    expect(search.value).toBe('some&info');

    fireEvent.submit(search);

    await out.findByTestId('home-map-display');

    expect(fetch.mock.calls.length).toBe(3);
    expect(fetch.mock.calls[2][0]).toBe('http://localhost:8080/search/maps?q=some%26info&per_page=10&page=1');
});
