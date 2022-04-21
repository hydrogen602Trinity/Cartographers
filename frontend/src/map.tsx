import { useNavigate, useParams } from "react-router-dom";
import NoMatch from "./no_match";
import { useMap } from "./util/api";
import "./map.scss";
import "./main.scss";
import { useEffect } from "react";

/**
 * Checks the validity of URL parameters before
 * rendering view. Renders NoMatch if the parameter is
 * missing or invalid.
 * @returns {JSX.Element} the view
 */
export function ParamFilter() {
  const params = useParams();

  const id = parseInt(params.id || '');
  if (!isFinite(id) || id < 0) {
    // invalid id: id must be non-negative and not infinity or NaN
    console.error(`invalid id: id=${id}, expected nonnegative number`);
    return <NoMatch />;
  }

  return <MapView id={id} />
}

interface IProps {
  id: number
}

/**
 * Display information about a single map
 * It gets information via url parameters
 * @returns {JSX.Element} the view
 */
function MapView({ id }: IProps) {
  // once the backend is updated to provide information on individual maps, replace this

  const [isLoading, map, err] = useMap(id);
  // console.log(`re-render ${id}`, [isLoading, map, err]);

  const nav = useNavigate();

  useEffect(() => {
    if (err && err.status !== 404) {
      nav(-1); // go back
    }
  })

  if (err && err.status === 404) {
    return <NoMatch />;
  }

  return ( //<p>{JSON.stringify(params)}</p>
    <div className="main map-view">
      <div className="maps">
        <div className="center">
          {(isLoading || err) ?
            (err ? <div>Error fetching data</div> : <div>Loading...</div>)
            :
            <div className="center">
              <img src={map?.image_url} alt={map?.name} />
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

export default MapView;