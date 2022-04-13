import { Grid, Pagination } from "@mui/material";
import MapCard from "./components/MapCard";
import "./main.scss";
import { useAllMaps } from "./util/api";

/**
 * View of home page
 * @returns {JSX.Element} the view
 */
function Main() {
  const [isLoading, maps, err] = useAllMaps();

  return (
    <div className="main">
      <div className="header center">
        <h1>CTM Repository</h1>
      </div>
      <div className="maps">
        <div className="center">
          {(isLoading || err) ?
            (
              /* If page is loading or errored, show messages instead */
              (isLoading) ?
                <div>Loading...</div> :
                <div>Error Loading Page</div>
            ) :
            <div>
              <Grid container spacing={2}>
                {maps.map((map, index) => (<MapCard map={map} key={index} />))}
              </Grid>
              <Pagination count={10} sx={{ marginTop: '2em' }}></Pagination>
            </div>
          }
        </div>
      </div>
    </div>
  );
}

export default Main;
