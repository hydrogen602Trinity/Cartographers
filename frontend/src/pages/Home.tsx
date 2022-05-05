import Divider from "@mui/material/Divider";
import Grid from "@mui/material/Grid";
import Pagination from "@mui/material/Pagination";
import Paper from "@mui/material/Paper";
import Stack from "@mui/material/Stack";
import { useState } from "react";
import { useSearchParams } from "react-router-dom";

import Card from "@mui/material/Card";
import CardMedia from "@mui/material/CardMedia";
import MapCard from "components/MapCard";
import SearchBar from "components/SearchBar";
import { useState } from "react";
import { useSearchParams } from "react-router-dom";
import { useGetMapsCount, useGetMapsSearch } from "utilities/api";
import { getPublicPath } from 'utilities/env';
import { computePageCount } from "utilities/paging";
import "./Home.scss";



/**
 * View of home page
 * @returns {JSX.Element} the view
 */
export default function Home() {
  const [page, setPage] = useState(1);

  const mapsPerPage = 10;

  const [searchParams, setSearchParams] = useSearchParams();
  let searchTerm = searchParams.get('q');

  const searchHandler = (term: string) => {
    setSearchParams({ q: term });
  };

  const [isLoadingCount, mapCount, errCount] = useGetMapsCount();
  const pageCount = computePageCount(mapCount, mapsPerPage);
  // useFetch has a bug where an empty string makes it not fetch data but pretend it did, so if searchTerm is empty,
  // it will use Infinity instead. An object cannot be used because React will complain
  const [isLoading, maps, err] = useGetMapsSearch(searchTerm || '', page, [searchTerm || Infinity], mapsPerPage);

  if (mapCountError) {
    return <ErrorPage error={mapCountError} />;
  }
  else if (searchError) {
    return <ErrorPage error={searchError} />;
  }
  else {
    return (
      <Grid container className="home-page-layout" alignItems="center" justifyContent="center" spacing={1}>
        <Grid item xs={12} md={8}>
          <Card className="map-card" elevation={0} style={{ backgroundColor: "transparent" }}>
            <CardMedia
              component="img"
              image={getPublicPath('/banner.webp')}
              alt="CTM Repository Banner"
            />
          </Card>
        </Grid>
        <Grid item xs={12} sm={10} md={8} marginLeft="1em" marginRight="1em">
          <SearchBar
            onSearch={searchHandler}
            defaultValue={searchTerm || ''} />
        </div>
        <div className="maps">
          <div className="center">
            {(isLoading || isLoadingCount || err || errCount) ?
              (
                /* If page is loading or errored, show messages instead */
                (isLoading || isLoadingCount) ?
                  <div>Loading...</div> :
                  <div>Error Loading Page</div>
              ) :
              <div data-testid='home-map-display'>
                <Grid container spacing={2}>
                  {maps.map((map, index) => (
                    <Grid item key={map.id} xs={12} sm={6} md={6} lg={4} xl={3}>
                      <MapCard map={map} key={index} />
                    </Grid>
                  ))}
                </Grid>
                <Pagination count={pageCount} sx={{ marginTop: '2em' }} page={page} onChange={(_, e) => { setPage(e) }}></Pagination>
              </div>
            }
          </div>
        </div>
      </div>
    </div>
  );
}
