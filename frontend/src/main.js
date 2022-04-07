import { Pagination } from "@mui/material";
import MapCard from "./components/MapCard";
import Grid from '@mui/material/Grid';
import "./main.scss";
import { useAllMaps } from "./util/api";


function Main() {
  const [isLoading, maps, err] = useAllMaps();
  // console.log(isLoading, maps, err);

  return (
    <div className="main">
      {/* Main {process.env.REACT_APP_API} */}
      <div className="header center"><h1>CTM Repository</h1></div>
      <div className="maps">
        {/* <Stack 
          spacing={2}
          justifyContent="center"
          alignItems="center">
          <MapCard />
          <MapCard />
          <MapCard />
        </Stack> */}
        <div className="center">
          {(isLoading || err) ?
            ((isLoading) ? <div>Loading...</div> : <div>Something went wrong...</div>) :
            <div>
              <Grid
                container
                spacing={{ xs: 2, md: 3 }}
                // columns={{ xs: 4, sm: 8, md: 12 }}
                direction="row"
                sx={{
                  alignItems: "center",
                  alignContent: "center",
                  justifyContent: "center",
                }}>

                {maps.map((map, index) => (
                  <Grid item xs={12} sm={6} md={4} key={index}>
                    <MapCard map={map} />
                  </Grid>
                ))}
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
