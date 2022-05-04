import Divider from "@mui/material/Divider";
import Grid from "@mui/material/Grid";
import Pagination from "@mui/material/Pagination";
import Paper from "@mui/material/Paper";
import Stack from "@mui/material/Stack";
import Box from "@mui/system/Box";
import { useState } from "react";
import { useSearchParams } from "react-router-dom";

import MapCard from "components/MapCard";
import SearchBar from "components/SearchBar";
import ErrorPage from "pages/ErrorPage";
import { useGetMapsCount, useGetMapsSearch } from "utilities/api";
import { getPublicPath } from 'utilities/env';
import { computePageCount } from "utilities/paging";


/**
 * View of home page
 * @returns {JSX.Element} the view
 */
export default function Home() {
  const [page, setPage] = useState(1);
  const [searchParams, setSearchParams] = useSearchParams();

  let searchTerm = searchParams.get('q');
  const searchHandler = (term: string) => {
    setSearchParams({ q: term });
  };

  const [isMapCountLoading, mapCount, mapCountError] = useGetMapsCount();
  const mapsPerPage = 12;
  const pageCount = computePageCount(mapCount, mapsPerPage);

  // useFetch has a bug where an empty string makes it not fetch data but pretend it did, so if searchTerm is empty,
  // it will use Infinity instead. An object cannot be used because React will complain
  const [isLoading, maps, searchError] = useGetMapsSearch(searchTerm || '', page, [searchTerm || Infinity], mapsPerPage);

  if (mapCountError) {
    return <ErrorPage error={mapCountError} />;
  }
  else if (searchError) {
    return <ErrorPage error={searchError} />;
  }
  else {
    return (
      <Grid container className="home-page-layout" alignItems="center" justifyContent="center" spacing={1}>
        <Grid item xs={12}>
          <Grid container alignItems="center" justifyContent="center">
            <Grid item>
              <Box
                component="img"
                className="ctm-repository-banner"
                src={getPublicPath('/banner.webp')}
                alt="CTM Repository"
              />
            </Grid>
          </Grid>
        </Grid>
        <Grid item xs={12} sm={10} md={8} marginLeft="1em" marginRight="1em">
          <SearchBar
            onSearch={searchHandler}
            defaultValue={searchTerm || ''}
          />
        </Grid>
        <Grid item xs={12} margin="1em">
          <Paper className="search-results-display" sx={{ p: 2 }}>
            <Stack
              alignItems="center"
              justifyContent="center"
              divider={<Divider orientation="horizontal" flexItem />}
              spacing={2}
            >
              {(isLoading || isMapCountLoading) ? (
                <div>Loading...</div>
              ) : (
                <div data-testid='home-map-display'>
                  <Grid container spacing={2}>
                    {maps.map((map, index) => (
                      <Grid item key={map.id} xs={12} sm={12} md={6} lg={4} xl={3} width="100%">
                        <MapCard map={map} key={index} />
                      </Grid>
                    ))}
                  </Grid>
                </div>
              )}
              <Pagination count={pageCount} page={page} onChange={(_, e) => { setPage(e) }} sx={{ marginTop: '1em' }} />
            </Stack>
          </Paper>
        </Grid>
      </Grid>
    );
  }
}
