import { Paper } from "@mui/material";
import { useParams } from "react-router-dom";
import NoMatch from "./no_match";
import { MCMap, useAllMaps } from "./util/api";
import "./map.scss";
import "./main.scss";
import { useMemo } from "react";

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

enum Status {
  NOT_YET_RECEIVED,
  MAP_MISSING,
  READY
}

/**
 * Display information about a single map
 * It gets information via url parameters
 * @returns {JSX.Element} the view
 */
function MapView({ id }: IProps) {
  // once the backend is updated to provide information on individual maps, replace this
  const [_, maps, err] = useAllMaps();

  const [status, map]: [Status, MCMap | null] = useMemo(() => {
    if (maps === undefined) {
      return [Status.NOT_YET_RECEIVED, null];
    }

    const candidates = maps.filter(m => m.id == id);
    if (candidates.length !== 1) {
      // invalid id: id not found
      console.error(`id=${id} not found in maps`);
      return [Status.MAP_MISSING, null];
    }

    return [Status.READY, candidates[0] || null];
  }, [maps]);

  console.log(map);

  if (status === Status.MAP_MISSING) {
    return <NoMatch />;
  }

  const content = (status === Status.READY) ? (
    <div>
      <img src={map?.image_url} alt={map?.name} />
    </div>
  ) : <div>Loading...</div>;

  return ( //<p>{JSON.stringify(params)}</p>
    <div className="main map-view">
      <div className="maps">
        <div className="center">
          {err ?
            <div>Error Loading Page</div>
            :
            content
          }
        </div>
      </div>
    </div>
  )
}

export default MapView;