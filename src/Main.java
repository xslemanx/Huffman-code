import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);

	}
	public String convert(long bytes ) {
        BigDecimal kilobytes = new BigDecimal(bytes).divide(new BigDecimal(1024));
        BigDecimal megabytes = kilobytes.divide(new BigDecimal(1024));
        BigDecimal gigabytes = megabytes.divide(new BigDecimal(1024));

        if (gigabytes.compareTo(BigDecimal.ONE) >= 0) {
            return(gigabytes.setScale(2, BigDecimal.ROUND_HALF_UP) + " GB");
        } else if (megabytes.compareTo(BigDecimal.ONE) >= 0) {
        	return(megabytes.setScale(2, BigDecimal.ROUND_HALF_UP) + " MB");
        } else if (kilobytes.compareTo(BigDecimal.ONE) >= 0) {
        	return(kilobytes.setScale(2, BigDecimal.ROUND_HALF_UP) + " KB");
        } else {
        	return(bytes + " bytes");
        }
	}
	@Override
	public void start(Stage arg0) throws Exception {
		Stage primaryStage = new Stage();
		Pane root = new Pane();// crate pane
		Scene scene = new Scene(root, 700, 500);// crate scene
		primaryStage.setTitle("the main menu");// set stage title
		root.setStyle("-fx-background-color: 			#778899	;\r\n");// set the pane color using css
		
		//Image image1 = new Image("C:\\Users\\pc-2022\\Downloads\\add file.gif");
		//ImageView imageView1 = new ImageView(image1);
		//imageView1.setFitHeight(50);
		//imageView1.setFitWidth(50);
		//imageView1.setVisible(true);
		
		//Image image2 = new Image("C:\\Users\\pc-2022\\Downloads\\add file.gif");
	//	ImageView imageView2 = new ImageView(image2);
	//	imageView2.setFitHeight(50);
		//imageView2.setFitWidth(50);
		//.setVisible(true);
	
		Label lb7 = new Label("compression done sucssefully");
		lb7.setFont(new Font("Arial", 22));
		lb7.setTextFill(Color.DARKBLUE);
		lb7.setTranslateX(200);
		lb7.setTranslateY(420);
		lb7.setVisible(false);
		
		Label lb8 = new Label("decompression done sucssefully");
		lb8.setFont(new Font("Arial", 22));
		lb8.setTextFill(Color.DARKBLUE);
		lb8.setTranslateX(200);
		lb8.setTranslateY(420);
		lb8.setVisible(false);
		
		
		Label lb9 = new Label("the file is not .huf extension");
		lb9.setFont(new Font("Arial", 22));
		lb9.setTextFill(Color.RED);
		lb9.setTranslateX(200);
		lb9.setTranslateY(420);
		lb9.setVisible(false);
		
		Label lb10 = new Label("null file cannot compress it");
		lb10.setFont(new Font("Arial", 22));
		lb10.setTextFill(Color.RED);
		lb10.setTranslateX(200);
		lb10.setTranslateY(420);
		lb10.setVisible(false);
		
		Image myimage1 = new Image(new FileInputStream("compress.png"));// add the image
		ImageView myview1 = new ImageView(myimage1);// show
		// set the size
		myview1.setFitHeight(50);
		myview1.setFitWidth(50);
		Button compress = new Button("compress   ");// create a button and add the image
		// set size and position
		compress.setTranslateX(120);
		compress.setTranslateY(20);
		compress.setMinWidth(200);
		compress.setMinHeight(80);
		compress.setMaxWidth(200);
		compress.setMaxHeight(80);
		// set the button color and radius using css
		compress.setStyle("-fx-background-color: #FFFFFF;\r\n" + "        -fx-background-radius:100;\r\n");
		compress.setTextFill(Color.DARKBLUE);// set text color
		compress.setFont(new Font("Arial", 20));// set font type
		
		 FileChooser fileChooser = new FileChooser();
		 
		 
		 TextArea info = new TextArea();
			info.setFont(new Font("Arial", 20));
			info.setTranslateX(70);
			info.setTranslateY(130);
			info.setMinSize(560, 270);
			info.setMaxSize(560, 270);
			info.setEditable(false);
			
		
			
			
			
		compress.setOnAction(e->{
			lb8.setVisible(false);
	    	lb7.setVisible(false);
	    	lb9.setVisible(false);
	    	lb10.setVisible(false);
	    	info.setText("");
			//fileChooser
			//imageView.setVisible(true);
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
			    String filePath=selectedFile.getPath();
			    try {
			    	Huffman.compress(new File(filePath));
			    	if(Huffman.fileIsNull==-1) {
			    		
					
					String Data=new String("File name : ");
					Data=Data+selectedFile.getName()+"\n";
					Data=Data+"orginal file size : "+convert(Huffman.fileSize)+"("+Huffman.fileSize+")"+" \ncompressed file size : "+convert(Huffman.compressedFileSize)+"("+Huffman.compressedFileSize+")"+"\n";
					int xa=0;
					Data=Data+"ASCII\t"+"character \t"+"Frequancey\t\t"+"Code \n\n";
					for(int i=0;i<Huffman.HffmanCodesArray.length;i++) {
						xa=((int)Huffman.HffmanCodesArray[i].character);
						
						Data=Data+xa+"\t\t\t";
						if(xa!=9&&xa!=10)
							Data=Data+(char)xa+"\t\t\t";
						if(xa==9)
							Data=Data+"\t\t\t";
						if(xa==10)
							Data=Data+"\t\t\t";
						
						Data=Data+Huffman.HffmanCodesArray[i].counter+"\t\t";
						Data=Data+Huffman.HffmanCodesArray[i].huffCode+"\n";
						
						lb7.setVisible(true);
					}
					info.setText(Data);
					}
			    	else {
			    		lb10.setVisible(true);
			    	}
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
			    // the user closed the dialog without selecting a file
			}
		
		});
		
		
		Image myimage2 = new Image(new FileInputStream("deCompress.png"));// add the image
		ImageView myview2 = new ImageView(myimage2);// show
		// set the size
		myview2.setFitHeight(50);
		myview2.setFitWidth(50);
		Button deCompress = new Button("decompress");// create a button and add the image
		// set size and position
		deCompress.setTranslateX(370);
		deCompress.setTranslateY(20);
		deCompress.setMinWidth(200);
		deCompress.setMinHeight(80);
		deCompress.setMaxWidth(200);
		deCompress.setMaxHeight(80);
		// set the button color and radius using css
		deCompress.setStyle("-fx-background-color: #FFFFFF;\r\n" + "        -fx-background-radius:100;\r\n");
		deCompress.setTextFill(Color.DARKBLUE);// set text color
		deCompress.setFont(new Font("Arial", 20));// set font type
		//compress.setDisable(true);
		deCompress.setOnAction(e->{
			lb8.setVisible(false);
	    	lb7.setVisible(false);
	    	lb9.setVisible(false);
	    	lb10.setVisible(false);
	    	info.setText("");
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
				String filePath=selectedFile.getPath();
				String fileName = selectedFile.getName();
				int extensionIndex = fileName.lastIndexOf(".");
				String fileExtension = extensionIndex == -1 ? "" : fileName.substring(extensionIndex + 1);
				if(fileExtension.equals("huf")) {
					lb9.setVisible(false);
					try {
						Huffman.decompress(new File(filePath));
						String Data=new String("File name : ");
						Data=Data+selectedFile.getName()+"\n";
						int xa=0;
						Data=Data+"ASCII\t"+"character \t"+"Code \n\n";
						for(int i=0;i<Huffman.HffmanCodesArrayDecompress.length;i++) {
							xa=((int)Huffman.HffmanCodesArrayDecompress[i].character);
							
							Data=Data+xa+"\t\t\t";
							if(xa!=9&&xa!=10)
								Data=Data+(char)xa+"\t\t";
							if(xa==9)
								Data=Data+"\t\t";
							if(xa==10)
								Data=Data+"\t\t";
							
						
							Data=Data+Huffman.HffmanCodesArrayDecompress[i].huffCode+"\n";
							
							
							
						}
						info.setText(Data);
						lb8.setVisible(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else {
					lb9.setVisible(true);
				}

			}
			
		});
		
		root.getChildren().addAll(compress,deCompress,info,lb7,lb8,lb9,lb10);// add the controls to the pane
		primaryStage.setScene(scene);// set the scene
		primaryStage.show();
		
		
	}

}
