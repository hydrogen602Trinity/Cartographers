import {
  Card, CardActionArea, CardContent, CardMedia,
  Grid, List, ListItem, ListItemText, Skeleton, Typography
} from "@mui/material";
import "pages/MapView.scss";
import { useNavigate, useParams } from "react-router-dom";
import ErrorPage from "types/ErrorPage";
import { useGetMap } from "utilities/api";
import { getPublicPath } from "utilities/env";

/**
 * Display information about a single map
 * It gets information via url parameters
 * Checks the validity of URL parameters before
 * rendering view. Renders NoMatch if the parameter is
 * missing or invalid.
 * @returns {JSX.Element} the view
 */
export default function MapView() {
  const params = useParams();
  const [isLoading, map, error] = useGetMap(parseInt(params.id || ""));
  const navigate = useNavigate();

  if (error) {
    return <ErrorPage error={error} />;
  }
  else {
    return (
      (isLoading || map == null) ?
        <Skeleton /> :
        <Grid
          container
          spacing={0}
          marginTop="5vh"
          justifyContent="center"
          style={{ minHeight: "100vh" }}
        >
          <Grid item xs={6}>
            <Card className="map-card">
              <CardActionArea onClick={_ => navigate("/")}>
                <CardMedia
                  component="img"
                  image={getPublicPath(map.image_url)}
                  alt="Map Image"
                />
                <CardContent>
                  <Typography gutterBottom variant="h5" component="div">
                    {map.name}
                  </Typography>
                  <List dense={true}>
                    <ListItem>
                      <ListItemText primary="Map Series" secondary={map.series} />
                    </ListItem>
                    <ListItem>
                      <ListItemText primary="Author" secondary={map.author} />
                    </ListItem>
                    <ListItem>
                      <ListItemText primary="Upload Date" secondary={map.upload_date} />
                    </ListItem>
                  </List>
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>
        </Grid >
    )
  }
}
