import { useNavigate, useParams } from "react-router-dom";
import NoMatch from "./NoMatch";
import "./MapView.scss";
import "../pages/Home.scss";
import { useEffect } from "react";
import { useMap } from "../utilities/api";
import { getPublicPath } from "../utilities/env";

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
  const id = parseInt(params.id || '');
  const [isLoading, map, err] = useMap(id);

  const nav = useNavigate();

  useEffect(() => {
    if (err && err.status !== 404) {
      nav(-1); // go back
    }
  })

  if (err && err.status === 404) {
    return <NoMatch />;
  }

  return (
    <div className="main map-view">
      <div className="maps">
        <div className="center">
          {(isLoading || err) ?
            (err ? <div>Error fetching data</div> : <div>Loading...</div>)
            :
            <div className="center">
              <img src={getPublicPath(map.image_url)} alt={map.name} />
              <ul>
                {(map) ? Object.keys(map).map(key => <li key={key}>{`${key}: ${((map as any)[key] || '')}`}</li>) : <></>}
              </ul>
            </div >
          }
        </div>
      </div>
    </div>
  )
}
