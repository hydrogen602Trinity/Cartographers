import { Grid, Pagination } from "@mui/material";
import { useState } from "react";
import MapCard from "./components/MapCard";
import SearchBar from "./components/SearchBar";
import "./main.scss";
import { useMapCount, useSearchMaps } from "./util/api";
import { computePageCount } from "./util/paging";

/**
 * View of home page
 * @returns {JSX.Element} the view
 */
function Main() {
  const [page, setPage] = useState(1);

  const mapsPerPage = 10;

  const [isLoadingCount, mapCount, errCount] = useMapCount();
  const pageCount = computePageCount(mapCount, mapsPerPage);
  const [isLoading, maps, err] = useSearchMaps('', page, [], mapsPerPage);

  return (
    <div className="main">
      <div className="header center">
        <img src={'/Cartographers/logo.webp'} alt="CTM Repository" />
      </div>
      <div className="content">
        <SearchBar></SearchBar>
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

export default Main;
