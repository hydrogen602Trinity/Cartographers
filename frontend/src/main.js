import { Pagination } from "@mui/material";
import MapCard from "./components/MapCard";
import "./main.scss";
import { useAllMaps } from "./util/api";

/**
 * View of home page
 * @returns {JSX.Element} the view
 */
function Main() {
  // loads all maps
  const [isLoading, maps, err] = useAllMaps();

  return (
    <div className="main">
      {/* page header */}
      <div className="header center"><h1>CTM Repository</h1></div>
      {/* body of page */}
      <div className="maps">
        <div className="center">
          {(isLoading || err) ?
            /* if its still loading or errored, show messages instead */
            ((isLoading) ? <div>Loading...</div> : <div>Something went wrong...</div>) :
            <div>
              {/* <Grid
                container
                spacing={{ xs: 2, md: 3 }}
                // columns={{ xs: 4, sm: 8, md: 12 }}
                direction="row"
                sx={{
                  alignItems: "center",
                  alignContent: "center",
                  justifyContent: "center",
                }}> */}
              <div className="map-grid">
                {maps.map((map, index) => (
                  <MapCard map={map} key={index} />
                ))}
                {/* </Grid> */}
              </div>
              <Pagination count={10} sx={{ marginTop: '2em' }}></Pagination>
            </div>
          }
        </div>
      </div>
    </div>
  );
}

export default Main;
