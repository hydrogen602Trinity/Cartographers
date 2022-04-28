import { Grid, Pagination } from "@mui/material";
import { useState } from "react";
import { useSearchParams } from "react-router-dom";
import MapCard from "../components/MapCard";
import SearchBar from "../components/SearchBar";
import { useMapCount, useSearchMaps } from "../utilities/api";
import { computePageCount } from "../utilities/paging";
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

  const [isLoadingCount, mapCount, errCount] = useMapCount();
  const pageCount = computePageCount(mapCount, mapsPerPage);
  // useFetch has a bug where an empty string makes it not fetch data but pretend it did, so if searchTerm is empty,
  // it will use Infinity instead. An object cannot be used because React will complain
  const [isLoading, maps, err] = useSearchMaps(searchTerm || '', page, [searchTerm || Infinity], mapsPerPage);

  return (
    <div className="main">
      <div className="header center">
        <img src={'/Cartographers/logo.webp'} alt="CTM Repository" />
      </div>
      <div className="content">
        <div className="search-bar-wrapper">
          <SearchBar
            callback={searchHandler}
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
              <div>
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
