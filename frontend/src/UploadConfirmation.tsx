import { useRef, useState, useEffect } from 'react';
import Button from '@mui/material/Button';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import Dialog from '@mui/material/Dialog';
import { PixelCrop } from 'react-image-crop';
import { canvasPreview } from './preview';


export interface ConfirmationDialogRawProps {
  id: string;
  keepMounted: boolean;
  open: boolean;
  onClose: (ok: boolean) => void;
  completedCrop: PixelCrop;
  image: HTMLImageElement;
}

function ConfirmationDialogRaw(props: ConfirmationDialogRawProps) {
  const { onClose, open, image, completedCrop, ...other } = props;

  const handleCancel = () => {
    onClose(false);
  };

  const handleOk = () => {
    onClose(true);
  };

  const previewCanvasRef = useRef<HTMLCanvasElement>(null);

  useEffect(() => {
    if (open && previewCanvasRef.current && completedCrop) {
      canvasPreview(
        image,
        previewCanvasRef.current,
        completedCrop
      );
    }
  }, [completedCrop, open]);

  return (
    <Dialog
      sx={{ '& .MuiDialog-paper': { width: '80%', maxHeight: 435 } }}
      maxWidth="xs"
      // TransitionProps={{ onEntering: handleEntering }}
      open={open}
      {...other}
    >
      <DialogTitle>Image to Upload</DialogTitle>
      <DialogContent dividers>
        <canvas
          width={completedCrop.width}
          height={completedCrop.height}
          ref={previewCanvasRef}
          style={{
            border: '1px solid black',
            objectFit: 'contain',
          }}
        />
      </DialogContent>
      <DialogActions>
        <Button autoFocus onClick={handleCancel}>
          Cancel
        </Button>
        <Button onClick={handleOk}>Ok</Button>
      </DialogActions>
    </Dialog>
  );
}

interface IProps {
  image: HTMLImageElement,
  completedCrop: PixelCrop,
}

export default function ConfirmationDialog({ completedCrop, image }: IProps) {
  const [open, setOpen] = useState(false);

  const handleClickListItem = () => {
    setOpen(true);
  };

  const handleClose = (ok: boolean) => {
    setOpen(false);
    console.log('Confirmation Success:', ok);
  };

  return (
    <>
      <Button onClick={handleClickListItem}>Test</Button>
      <ConfirmationDialogRaw
        id="upload-confirm"
        keepMounted
        open={open}
        completedCrop={completedCrop}
        image={image}
        onClose={handleClose} />
    </>
  );
}