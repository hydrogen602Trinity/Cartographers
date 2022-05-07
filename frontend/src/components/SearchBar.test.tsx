import '@testing-library/jest-dom';
import { render, waitFor } from '@testing-library/react';
import user from '@testing-library/user-event';

import SearchBar from 'components/SearchBar';

/**
 * Ensures SearchBar renders default components when provided
 * with an empty string for the 'defaultValue' parameter
 */
test("Check default SearchBar component visibility", async () => {
    const searchBar = render(
        <SearchBar
            onSearch={() => { }}
            defaultValue={""}
        />
    );

    await waitFor(() => searchBar.findByTestId("search-bar-base"));

    const searchButton = searchBar.getByLabelText("Submit search query");
    expect(searchButton).toBeInTheDocument();
    expect(searchButton).toBeVisible();

    const searchField = searchBar.getByLabelText("Search for CTM maps");
    expect(searchField).toBeInTheDocument();
    expect(searchField).toBeVisible();

    const clearSearchField = searchBar.queryByLabelText("Clear search field");
    expect(clearSearchField).toBeNull();

    const searchOptionMenu = searchBar.getByLabelText("Open search options menu");
    expect(searchOptionMenu).toBeInTheDocument();
    expect(searchOptionMenu).toBeVisible();
});

test("Check onSearch event", async () => {
    const callback = jest.fn();

    const searchBar = render(
        <SearchBar
            onSearch={callback}
            defaultValue={""}
        />
    );

    await waitFor(() => searchBar.findByTestId("search-bar-base"));

    const searchInput = searchBar.getByRole("textbox");
    user.type(searchInput, "test");
    expect(searchInput).toHaveValue("test");

    const searchButton = searchBar.getByLabelText("Submit search query");
    user.click(searchButton);
    await waitFor(() => expect(callback).toHaveBeenCalledTimes(1));
    expect(searchInput).toHaveValue("test");

    const clearSearchField = searchBar.getByLabelText("Clear search field");
    user.click(clearSearchField);
    await waitFor(() => expect(callback).toHaveBeenCalledTimes(2));
    expect(searchInput).toHaveValue("");
});
