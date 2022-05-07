import '@testing-library/jest-dom';
import { render, waitFor } from '@testing-library/react';
import user from '@testing-library/user-event';

import SearchBar from 'components/SearchBar';

/**
 * Ensures SearchBar renders default components when provided
 * with an empty string for the 'defaultValue' parameter
 */
test("Check default SearchBar component visibility", async () => {
    let searchResult = "";
    const handleSearch = (query: string) => { searchResult = query };

    let searchBar = render(
        <SearchBar
            onSearch={handleSearch}
            defaultValue={""}
        />
    );
    expect(searchResult).toBe("");

    await waitFor(() => searchBar.findByTestId("search-bar-base"));

    let searchButton = searchBar.getByLabelText("Submit search query");
    expect(searchButton).toBeInTheDocument();
    expect(searchButton).toBeVisible();

    let searchField = searchBar.getByLabelText("Search for CTM maps");
    expect(searchField).toBeInTheDocument();
    expect(searchField).toBeVisible();

    let clearSearchField = searchBar.queryByLabelText("Clear search field");
    expect(clearSearchField).toBeNull();

    let searchOptionMenu = searchBar.getByLabelText("Open search options menu");
    expect(searchOptionMenu).toBeInTheDocument();
    expect(searchOptionMenu).toBeVisible();
});

test("Check onSearch event", async () => {
    let searchResult = "";
    const handleSearch = (query: string) => { searchResult = query };

    let searchBar = render(
        <SearchBar
            onSearch={handleSearch}
            defaultValue={""}
        />
    );
    expect(searchResult).toBe("");

    await waitFor(() => searchBar.findByTestId("search-bar-base"));

    const input = searchBar.getByLabelText("Search for CTM maps")
    user.type(input, "test")
    expect(searchResult).toBe("");

    let searchButton = searchBar.getByLabelText("Submit search query");
    user.click(searchButton);
    expect(searchResult).toBe("test");

    let clearSearchField = searchBar.getByLabelText("Clear search field");
    expect(clearSearchField).toBeInTheDocument();
    expect(clearSearchField).toBeVisible();
    user.click(clearSearchField);
    expect(searchResult).toBe("");
});
