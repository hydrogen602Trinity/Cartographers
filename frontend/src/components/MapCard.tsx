import AccessTimeIcon from '@mui/icons-material/AccessTime';
import Card from '@mui/material/Card';
import CardActionArea from '@mui/material/CardActionArea';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import DownloadIcon from '@mui/icons-material/Download';
import HistoryIcon from '@mui/icons-material/History';
import PersonIcon from '@mui/icons-material/Person';
import Typography from '@mui/material/Typography';

import { MCMap } from '../utilities/api';
import { getPublicPath } from '../utilities/env';
import "./MapCard.scss";
import { useNavigate } from 'react-router-dom';

interface IProps {
  map: MCMap
}

/**
 * Creates a Card component given a map, displaying the most essential properties
 * @param {{ map: MCMap }} props The map to display
 * @returns {JSX.Element} the view
 */
export default function MapCard({ map }: IProps): JSX.Element {
  const id = map.id;
  const name = map.name;

  const nav = useNavigate();

  const expand = () => {
    nav('/maps/' + encodeURIComponent(id) + '/' +
      encodeURIComponent(name.toLowerCase().replace(' ', '-')));
  }

  return (
    <Card className="map-card">
      <CardActionArea onClick={expand}>
        <CardMedia
          component="img"
          image={getPublicPath(map.image_url)}
          alt="Map Image"
        />
        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {map.name}
          </Typography>
          <div className='info-group'>
            <InfoPiece key='author' text={map.author}><PersonIcon /></InfoPiece>
            <InfoPiece key='mc_version' text={map.mc_version}><HistoryIcon /></InfoPiece>
            <InfoPiece key='length' text={map.length}><AccessTimeIcon /></InfoPiece>
            <InfoPiece key='download_count' text={map.download_count + ''}><DownloadIcon /></InfoPiece>
          </div>
        </CardContent>
      </CardActionArea>
    </Card>
  );
}

interface InfoPieceProps {
  text: string,
  children: JSX.Element
}

/**
 * A helper function for MapCard
 * @param {{ text: String, children: JSX.Element }} props the text and other content to display
 * @returns the view
 */
function InfoPiece({ text, children }: InfoPieceProps) {
  return <div className="info">
    {children}
    <Typography variant="subtitle1" className='info-text'>
      {text}
    </Typography>
  </div>;
}
