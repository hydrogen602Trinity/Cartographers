import { PixelCrop } from 'react-image-crop';

const TO_RADIANS = Math.PI / 180

export async function canvasPreview(
  image: HTMLImageElement,
  canvas: HTMLCanvasElement,
  crop: PixelCrop,
) {
  // this code is derived from a code example from the react-image-crop library.

  const ctx = canvas.getContext('2d');

  if (!ctx) {
    throw new Error('No 2d context');
  }

  canvas.height = canvas.width;

  const scaleX = image.naturalWidth / image.width;
  const scaleY = image.naturalHeight / image.height;
  // devicePixelRatio slightly increases sharpness on retina devices
  // at the expense of slightly slower render times and needing to
  // size the image back down if you want to download/upload and be
  // true to the images natural size.
  // const pixelRatio = window.devicePixelRatio;

  // canvas.width = Math.floor(crop.width * scaleX * pixelRatio)
  // canvas.height = Math.floor(crop.height * scaleY * pixelRatio)

  // ctx.scale(pixelRatio, pixelRatio);
  ctx.imageSmoothingQuality = 'high';

  // const cropX = crop.x * scaleX;
  // const cropY = crop.y * scaleY;

  // const rotateRads = rotate * TO_RADIANS;
  // const centerX = image.naturalWidth / 2;
  // const centerY = image.naturalHeight / 2;

  // ctx.save();

  // // 5) Move the crop origin to the canvas origin (0,0)
  // ctx.translate(-cropX, -cropY);
  // // 4) Move the origin to the center of the original position
  // ctx.translate(centerX, centerY);
  // // 3) Rotate around the origin
  // ctx.rotate(rotateRads);
  // // 2) Scale the image
  // ctx.scale(scale, scale);
  // // 1) Move the center of the image to the origin (0,0)
  // ctx.translate(-centerX, -centerY);
  // ctx.drawImage(
  //   image,
  //   0,
  //   0,
  //   image.naturalWidth,
  //   image.naturalHeight,
  //   0,
  //   0,
  //   image.naturalWidth,
  //   image.naturalHeight,
  // );

  // let scale = 2;

  // ctx.scale(2, 2);

  [canvas.width, canvas.height] = [600, 600];
  //[canvas.width * 2, canvas.height * 2];

  ctx.drawImage(
    image,
    crop.x,
    crop.y,
    crop.width,
    crop.height,
    0,
    0,
    canvas.width,
    canvas.height,
  );

  // ctx.restore();
}
