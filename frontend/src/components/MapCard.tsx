import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import { CardActionArea } from '@mui/material';
import { MCMap } from '../util/api';
import PersonIcon from '@mui/icons-material/Person';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import HistoryIcon from '@mui/icons-material/History';
import DownloadIcon from '@mui/icons-material/Download';
import "./MapCard.scss"

interface IProps {
  map: MCMap
}

/**
 * Creates a Card component given a map, displaying the most essential properties
 * @param {{ map: MCMap }} props The map to display
 * @returns {JSX.Element} the view
 */
export default function MapCard({ map }: IProps) {
  return (
    <Card sx={{ maxWidth: 'min(95vw, 600px)', minWidth: 'max(30vw, 300px)' }} className="mapCard">
      <CardActionArea>
        <CardMedia
          component="img"
          height="140"
          image={map.image_url}
          alt="image of map"
        />
        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {map.name}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {map.description_short}
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

