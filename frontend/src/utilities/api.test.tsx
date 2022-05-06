import fetch from 'jest-fetch-mock';
import { MinecraftMap, useGetMap, useGetMapsSearch } from 'utilities/api';
import { APITest, ExpectedResult } from 'utilities/testUtilities';


fetch.enableMocks();

beforeEach(() => {
    fetch.resetMocks();
    process.env.REACT_APP_PRODUCTION_API = "http://localhost:8080";
    process.env.REACT_APP_DEVELOPMENT_API = "http://localhost:8080";
});

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

test("useGetMapsSearch", async () => {
    fetch.mockResponseOnce(JSON.stringify([m]));

    let x = await APITest(ExpectedResult.Success, useGetMapsSearch, 'some/query');
    expect(x).toBe(JSON.stringify([m]));

    expect(fetch.mock.calls.length).toEqual(1);

    expect(fetch.mock.calls[0][0]).toBe("http://localhost:8080/search/maps?q=some%2Fquery&per_page=12&page=0");
});

test("useGetMapsSearch", async () => {
    fetch.mockResponseOnce(JSON.stringify([m]));

    let x = await APITest(ExpectedResult.Success, useGetMapsSearch, 'some/query', 42);
    expect(x).toBe(JSON.stringify([m]));

    expect(fetch.mock.calls.length).toEqual(1);

    expect(fetch.mock.calls[0][0]).toBe("http://localhost:8080/search/maps?q=some%2Fquery&per_page=12&page=42");
});

test("useGetMapsSearch", async () => {
    fetch.mockResponseOnce(JSON.stringify([m]));

    let x = await APITest(ExpectedResult.Success, useGetMapsSearch, 'some/query', 42, null, 1);
    expect(x).toBe(JSON.stringify([m]));

    expect(fetch.mock.calls.length).toEqual(1);

    expect(fetch.mock.calls[0][0]).toBe("http://localhost:8080/search/maps?q=some%2Fquery&per_page=1&page=42");
});

test("useGetMap", async () => {
    fetch.mockResponseOnce(JSON.stringify(m));

    let x = await APITest(ExpectedResult.Success, useGetMap, 30);
    expect(x).toBe(JSON.stringify(m));

    expect(fetch.mock.calls.length).toEqual(1);

    expect(fetch.mock.calls[0][0]).toBe("http://localhost:8080/maps/30");
});


