import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import { CardActionArea } from '@mui/material';
import { MCMap } from '../util/api';

interface IProps {
  map: MCMap
}

export default function MapCard({ map }: IProps) {
  return (
    <Card sx={{ maxWidth: 345, minWidth: 250 }}>
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
        </CardContent>
      </CardActionArea>
    </Card>
  );
}
