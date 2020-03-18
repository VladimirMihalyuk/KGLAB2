import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.bmp.BmpHeaderDirectory;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.file.FileTypeDirectory;
import com.drew.metadata.gif.GifHeaderDirectory;
import com.drew.metadata.jfif.JfifDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.pcx.PcxDirectory;
import com.drew.metadata.png.PngDirectory;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

//"D:\\univer\\6-sem\\CG\\img"

public class Main {
    public static void main(String[] args) {

        String key = "1";
        Scanner scanner = new Scanner(System.in);
        while(key != "0"){
            System.out.println("1 - Read from package\n" +
                    "2 - Read file\n" +
                    "0 - End");
            key = scanner.nextLine();
            switch (key){
                case "1":
                    System.out.println("Please enter package name");
                    File dir = new File(scanner.nextLine());
                    File[] directoryListing = dir.listFiles();
                    if(directoryListing != null){
                        for(File file : directoryListing){
                            System.out.println(file.getName());
                            try {
                                getInfo(file);
                            } catch (ImageProcessingException e) {
                                System.out.println("Image Processing Exception ");
                            } catch (IOException e) {
                                System.out.println("There is no such file");
                            }
                        }
                    }else{

                    }

                    break;
                case "2":
                    System.out.println("Please enter file name");
                    File file = new File(scanner.nextLine());

                    System.out.println(file.getName());
                    try {
                        getInfo(file);
                    } catch (ImageProcessingException e) {
                        System.out.println("Image Processing Exception ");
                    } catch (IOException e) {
                        System.out.println("There is no such file");
                    }
                    break;
                case "0":
                    System.exit(0);
                    break;
            }
        }



    }




    static void getInfo(File file) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        FileTypeDirectory fileTypeDirectory
                = metadata.getFirstDirectoryOfType(FileTypeDirectory.class);
        switch (fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_MIME_TYPE)){
            case "image/gif":
                getGifInfo(metadata);
                break;
            case "image/jpeg":
                getJpegInfo(metadata);
                break;
            case "image/tiff":
                getTiffInfo(metadata);
                break;
            case "image/x-pcx":
                getXpcxInfo(metadata);
                break;
            case "image/png":
                getPngInfo(metadata);
                break;
            case "image/bmp":
                getBmpInfo(metadata);
                break;
        }

        System.out.println("-----------------------------------------------------");

    }

    static void getGifInfo(Metadata metadata){
        GifHeaderDirectory gifHeaderDirectory = metadata.getFirstDirectoryOfType(GifHeaderDirectory.class);
        System.out.println("Width - " + gifHeaderDirectory.getString(GifHeaderDirectory.TAG_IMAGE_WIDTH));
        System.out.println("Height - " + gifHeaderDirectory.getString(GifHeaderDirectory.TAG_IMAGE_HEIGHT));
        System.out.println("Color depth - " + gifHeaderDirectory.getString(GifHeaderDirectory.TAG_BITS_PER_PIXEL));
        System.out.println("Background Color Index - " + gifHeaderDirectory.getString(GifHeaderDirectory.TAG_BACKGROUND_COLOR_INDEX));
    }




    static void getJpegInfo(Metadata metadata){
        JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
        JfifDirectory jfifDirectory = metadata.getFirstDirectoryOfType(JfifDirectory.class);

        System.out.println("Width - " + jpegDirectory.getString(JpegDirectory.TAG_IMAGE_WIDTH));
        System.out.println("Height - " + jpegDirectory.getString(JpegDirectory.TAG_IMAGE_HEIGHT));

        if(jfifDirectory != null && jfifDirectory.getString(JfifDirectory.TAG_RESX) != null){
            System.out.println("Resolution - " + jfifDirectory.getString(JfifDirectory.TAG_RESX));
        }else{
            ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            System.out.println("Resolution - " + exifIFD0Directory.getString(ExifIFD0Directory.TAG_X_RESOLUTION));
        }

        System.out.println("Compression - " + jpegDirectory.getDescription(JpegDirectory.TAG_COMPRESSION_TYPE));
        System.out.println(jpegDirectory.getString(JpegDirectory.TAG_COMPONENT_DATA_1));
        System.out.println(jpegDirectory.getString(JpegDirectory.TAG_COMPONENT_DATA_2));
        System.out.println(jpegDirectory.getString(JpegDirectory.TAG_COMPONENT_DATA_3));
    }




    static void getTiffInfo(Metadata metadata){
        ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        System.out.println("Width - " + exifIFD0Directory.getString(ExifIFD0Directory.TAG_IMAGE_WIDTH));
        System.out.println("Height - " + exifIFD0Directory.getString(ExifIFD0Directory.TAG_IMAGE_HEIGHT));
        System.out.println("Resolution - " + exifIFD0Directory.getString(ExifIFD0Directory.TAG_X_RESOLUTION));
        System.out.println("Compression - " + exifIFD0Directory.getDescription(ExifIFD0Directory.TAG_COMPRESSION));
        System.out.println("Color depth - " + exifIFD0Directory.getString(ExifIFD0Directory.TAG_BITS_PER_SAMPLE));
        System.out.println("Samples Per Pixel - " + exifIFD0Directory.getString(ExifIFD0Directory.TAG_SAMPLES_PER_PIXEL));
    }

    static void getXpcxInfo(Metadata metadata){
        PcxDirectory pcxDirectory = metadata.getFirstDirectoryOfType(PcxDirectory.class);
        System.out.println("Width - " + pcxDirectory.getString(PcxDirectory.TAG_XMAX) + 1);
        System.out.println("Height - " + pcxDirectory.getString(PcxDirectory.TAG_YMAX) + 1);
        System.out.println("Resolution - " + pcxDirectory.getString(PcxDirectory.TAG_HORIZONTAL_DPI));
        System.out.println("Bytes Per Line - " + pcxDirectory.getString(PcxDirectory.TAG_BYTES_PER_LINE));
        System.out.println("Bytes Per Line - " + pcxDirectory.getString(PcxDirectory.TAG_PALETTE_TYPE));
    }

    static void getPngInfo(Metadata metadata){
        PngDirectory pngDirectory = metadata.getFirstDirectoryOfType(PngDirectory.class);
        System.out.println("Width - " + pngDirectory.getString(PngDirectory.TAG_IMAGE_WIDTH));
        System.out.println("Height - " + pngDirectory.getString(PngDirectory.TAG_IMAGE_HEIGHT));
        System.out.println("Compression - " +  pngDirectory.getDescription(PngDirectory.TAG_COMPRESSION_TYPE));
        System.out.println("Color depth - " + pngDirectory.getString(PngDirectory.TAG_BITS_PER_SAMPLE));
        System.out.println("Color type - " + pngDirectory.getString(PngDirectory.TAG_COLOR_TYPE));
    }

    static void getBmpInfo(Metadata metadata){
        BmpHeaderDirectory bmpHeaderDirectory = metadata.getFirstDirectoryOfType(BmpHeaderDirectory.class);
        System.out.println("Width - " + bmpHeaderDirectory.getString(BmpHeaderDirectory.TAG_IMAGE_WIDTH));
        System.out.println("Height - " + bmpHeaderDirectory.getString(BmpHeaderDirectory.TAG_IMAGE_HEIGHT));
        System.out.println("Compression - " + bmpHeaderDirectory.getDescription(BmpHeaderDirectory.TAG_COMPRESSION));
        System.out.println("Color depth - " + bmpHeaderDirectory.getString(BmpHeaderDirectory.TAG_BITS_PER_PIXEL));
        System.out.println("Bitmap type - " + bmpHeaderDirectory.getString(BmpHeaderDirectory.TAG_BITMAP_TYPE));
    }
}
