package com.jhu.app;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.json.JSONObject;

import com.jhu.azureapi.ApiUtil;
import com.jhu.azureapi.ComputerVisionAPI;
import com.jhu.azureapi.ComputerVisionWrapper;
import com.jhu.azureapi.DomainModel;


public class ComputerVisionApp extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
     * Creates new application
     */
    public ComputerVisionApp() {
        initComponents();
    }

    private void initComponents() {

    	//Define UI components  
        tabbedPane = new JTabbedPane();
        analyzePanel = new JPanel();
        titleLabel = new JLabel();
        apiComboBox = new JComboBox<>();
        
        instructionLabel = new JLabel();
        
        imagePromptLabel = new JLabel();
        imageURLTextField = new JTextField();
        fileBrowseButton = new JButton();
        analyzeImageButton = new JButton();
        
        responseLabel = new JLabel();
        sourceImageLabel = new JLabel();
        thumbnailLabel = new JLabel();
        
        responseScrollPane = new JScrollPane();
        responseTextArea = new JTextArea();
        sourceImage = new JLabel();
        thumbnailImage = new JLabel();
        
        captionLabel = new JLabel();
        
        sKeyLabel = new JLabel();
        sKeyTextField = new JTextField();
        sRegionLabel = new JLabel();
        sRegionComboBox = new JComboBox<>();
        setSubscriptionButton = new JButton();
        resetDefaultButton = new JButton("Reset Default");
        
        jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Select an image");
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Supported images","jpg","png", "gif","bmp","jpeg");
		jfc.addChoosableFileFilter(filter);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 640));
    
        titleLabel.setFont(new Font("Arial", 1, 16));
        titleLabel.setText("Computer Vision API:");

        instructionLabel.setText("Enter the URL or a file to an image, then click the \"Analyze Image\" button. ");

        imagePromptLabel.setText("Image URL or File:");
        fileBrowseButton.setText("Browse");
        analyzeImageButton.setText("Analyze Image");
      
        responseLabel.setText("Response:");
        sourceImageLabel.setText("Source image:");

        responseTextArea.setEditable(false);
        responseTextArea.setColumns(24);
        responseTextArea.setRows(6);
        responseScrollPane.setViewportView(responseTextArea);
        
        thumbnailLabel.setText("Thumbnail:");

        apiComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "analyze", "landmarks", "celebrities", "tag", "ocr", "thumbnail", "recognize" }));

        //UI Layout
		GroupLayout analyzePanelLayout = new GroupLayout(analyzePanel);
		analyzePanel.setLayout(analyzePanelLayout);
		
		analyzePanelLayout.setHorizontalGroup(analyzePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(analyzePanelLayout.createSequentialGroup().addContainerGap().addGroup(analyzePanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(analyzePanelLayout.createSequentialGroup()
								    .addGroup(analyzePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(responseScrollPane, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
										.addComponent(responseLabel))
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(analyzePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(analyzePanelLayout.createSequentialGroup()
												.addComponent(sourceImageLabel).addGap(0, 400, Short.MAX_VALUE))
										        .addComponent(sourceImage, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										        .addComponent(captionLabel, GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(analyzePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(thumbnailLabel)
										        .addComponent(thumbnailImage, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)))
						.addGroup(analyzePanelLayout.createSequentialGroup()
								.addGroup(analyzePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(analyzePanelLayout.createSequentialGroup()
										.addComponent(imagePromptLabel)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(imageURLTextField, GroupLayout.PREFERRED_SIZE,
												240, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(fileBrowseButton).addComponent(analyzeImageButton))
								.addGroup(analyzePanelLayout.createSequentialGroup()
										.addComponent(titleLabel)
										.addComponent(apiComboBox, GroupLayout.PREFERRED_SIZE,
												240, GroupLayout.PREFERRED_SIZE) )
								.addComponent(instructionLabel))
								.addGap(0, 0, Short.MAX_VALUE)))
						.addContainerGap()));
		analyzePanelLayout.setVerticalGroup(analyzePanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(analyzePanelLayout.createSequentialGroup()
						
						.addContainerGap()
						.addGroup(analyzePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(titleLabel)
								.addComponent(apiComboBox))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(instructionLabel).addGap(15, 15, 15)
						.addGroup(analyzePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(imagePromptLabel)
								.addComponent(imageURLTextField, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(fileBrowseButton)
								.addComponent(analyzeImageButton))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(analyzePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(responseLabel)
								.addComponent(sourceImageLabel)
								.addComponent(thumbnailLabel))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(analyzePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(responseScrollPane, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
								.addGroup(analyzePanelLayout.createSequentialGroup()
										.addComponent(sourceImage, GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(captionLabel, GroupLayout.PREFERRED_SIZE, 14,GroupLayout.PREFERRED_SIZE))
								.addComponent(thumbnailImage,GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));

		tabbedPane.addTab("Image Analyze", analyzePanel);

        sKeyLabel.setText("Subscription Key:");
        sKeyTextField.setText("Copy/paste your subscription key here.");
        sRegionLabel.setText("Subscription Region:");
        sRegionComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "eastus", "eastus2", "westcentralus", "westus","westus2", "westeurope", "southeastasia" }));
        setSubscriptionButton.setText("Set Subscription");
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(sKeyLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sKeyTextField, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sRegionLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sRegionComboBox, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
                        .addComponent(setSubscriptionButton)
                        .addComponent(resetDefaultButton)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tabbedPane)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(sKeyLabel)
                    .addComponent(sKeyTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sRegionLabel)
                    .addComponent(sRegionComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(setSubscriptionButton)
                    .addComponent(resetDefaultButton))
                .addGap(3, 3, 3))
        );

        pack();
        
        // Add all actions here
        imageURLTextField.addActionListener(new ActionListener() {
        	 public void actionPerformed(ActionEvent evt) {
     			displayImage(imageURLTextField.getText());
             }
        });
        
        analyzeImageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	analyzeButtonActionPerformed(evt);
            }
        });
        
        fileBrowseButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		if (ifcLastPath != null) {
        			jfc.setCurrentDirectory(ifcLastPath);
        		}
        		int returnValue = jfc.showOpenDialog(null);
        		if (returnValue == JFileChooser.APPROVE_OPTION) {
        			System.out.println(jfc.getSelectedFile().getPath());
        			imageURLTextField.setText(jfc.getSelectedFile().getPath());
        			ifcLastPath = jfc.getSelectedFile().getParentFile();
        			displayImage(jfc.getSelectedFile().getPath());
        			
        		}
        	}
        });
        
        setSubscriptionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	setSubscription(evt);
            }
        });
        
        resetDefaultButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	resetDefault(evt);
            }
        });
    }

  
    private void analyzeButtonActionPerformed(ActionEvent evt) {
    	String selectedAPI = (String)apiComboBox.getSelectedItem();
    	switch (selectedAPI) {
		case "analyze":
			analyzeImage();
			break;
		case "landmarks":
			landmarks();
			break;
		case "thumbnail":
			getThumbnail();
			break;
		case "celebrities":
			celebrities();
			break;
		case "tag":
			tagImage();
			break;
		case "ocr":
			ocrImage();
			break;
		case "recognize":
			recognizeText();
			break;
		default:
			break;
		}
    }
    
    private void setSubscription(ActionEvent evt) {
    	ComputerVisionAPI origAPI = api.getApi();
    	String skey = this.sKeyTextField.getText();
		String region = (String)sRegionComboBox.getSelectedItem();
		if(ApiUtil.isValidKey(skey) ){
			origAPI.setSubscriptionKey(skey);
			origAPI.changeRegion(region);
		} else {
			origAPI.reset();
			captionLabel.setText("");
		    responseTextArea.setText("Change subscription key is not valid. Reset to default subscription. ");
		}
    }
    
    private void resetDefault(ActionEvent evt) {
    	ComputerVisionAPI origAPI = api.getApi();
    	origAPI.reset();
    	sKeyTextField.setText(origAPI.getSubscriptionKey());
    	sRegionComboBox.setSelectedItem(origAPI.getRegion());
    }
    
    private void analyzeImage() {
    	  String imageUrl =  imageURLTextField.getText();
    	  if (!isGoodImageUrl(imageUrl)) {
    		  return;
    	  }
    	  displayImage(imageUrl);
          
          JSONObject jsonObj = api.analyzeImage(imageUrl);
          
          if (jsonObj == null) {
              return;
          }

          // Format and display the JSON response.
          responseTextArea.setText(jsonObj.toString(2));
          captionLabel.setText(getCaptionText(jsonObj,"description", "captions", "text"));
    }

    private void celebrities() {
      String imageUrl =  this.imageURLTextField.getText();
   	  if (!isGoodImageUrl(imageUrl)) {
   		  return;
   	  }
   	  displayImage(imageUrl);
         
         JSONObject jsonObj = api.analyzeDomainModel(imageUrl, DomainModel.CELEBRITIES);
         
         if (jsonObj == null) {
        	 this.responseTextArea.setText("No result");
             return;
         }
         // Format and display the JSON response.
         responseTextArea.setText(jsonObj.toString(2));
         
         captionLabel.setText(getCaptionText(jsonObj,"result","celebrities", "name"));

    }
    
    private void landmarks() {
        String imageUrl =  this.imageURLTextField.getText();
     	  if (!isGoodImageUrl(imageUrl)) {
     		  return;
     	  }
     	  displayImage(imageUrl);
           
           JSONObject jsonObj = api.analyzeDomainModel(imageUrl, DomainModel.LANDMARKS);
           
           if (jsonObj == null) {
          	 this.responseTextArea.setText("No result");
               return;
           }
           // Format and display the JSON response.
           responseTextArea.setText(jsonObj.toString(2));
           captionLabel.setText(getCaptionText(jsonObj,"result","landmarks", "name"));

      }
    
	
	private void getThumbnail() {
		String imageUrl = imageURLTextField.getText();
		if (!isGoodImageUrl(imageUrl)) {
			return;
		}
		displayImage(imageUrl);
		// Get the thumbnail for the image.
		try {
			InputStream in = api.generateThumbnail(imageUrl);
			if (in != null) {
				BufferedImage thumbnail = ImageIO.read(api.generateThumbnail(imageUrl));

				// Display the thumbnail.
				if (thumbnail != null) {
					scaleAndShowImage(thumbnail, thumbnailImage);
				} else {
					responseTextArea.setText("Error process thumbnail");
					return;
				}
			}
		} catch (Exception e) {
			responseTextArea.setText("Error process thumbnail: " + e);
			return;
		}
	}
    
    private void tagImage() {
  	  String imageUrl =  imageURLTextField.getText();
  	  if (!isGoodImageUrl(imageUrl)) {
  		  return;
  	  }
  	  displayImage(imageUrl);
        
        JSONObject jsonObj = api.tagImage(imageUrl);
        
        if (jsonObj == null) {
            return;
        }

        // Format and display the JSON response.
        responseTextArea.setText(jsonObj.toString(2));
        captionLabel.setText(getCaptionText(jsonObj, "tags", "name"));
  }
    
    private void ocrImage() {
    	  String imageUrl =  imageURLTextField.getText();
    	  if (!isGoodImageUrl(imageUrl)) {
    		  return;
    	  }
    	  displayImage(imageUrl);
          
          JSONObject jsonObj = api.ocrImage(imageUrl);
          
          if (jsonObj == null) {
              return;
          }

          // Format and display the JSON response.
          responseTextArea.setText(jsonObj.toString(2));
    }
    
    
    private void recognizeText() {
  	  String imageUrl =  imageURLTextField.getText();
  	  if (!isGoodImageUrl(imageUrl)) {
  		  return;
  	  }
  	  displayImage(imageUrl);
        
        JSONObject jsonObj = api.recognizeText(imageUrl);
        
        if (jsonObj == null) {
            return;
        }

        // Format and display the JSON response.
        responseTextArea.setText(jsonObj.toString(2));
        getCaptionText(jsonObj, "recognitionResult", "lines", "text");
  }
    
    //Helper methods 
    private boolean isGoodImageUrl(String imageUrl) {
    	boolean flag = true;
    	if (imageUrl == null || imageUrl.trim().length() < 3) {
      		responseTextArea.setText("Image URL or file name is invalid ");
      		flag  = false;
      	}
    	return flag;
    }
    
    private String getCaptionText(JSONObject jsonObj, String firstField, String secondField, String thirdField) {
		String captionText = null;
		if (jsonObj.has(firstField) && jsonObj.getJSONObject(firstField).has(secondField)
				&& jsonObj.getJSONObject(firstField).getJSONArray(secondField).length() > 0) {

			JSONObject jsonCaption = jsonObj.getJSONObject(firstField).getJSONArray(secondField).getJSONObject(0);

			if (jsonCaption.has(thirdField)) {
				captionText = "Caption: " + jsonCaption.getString(thirdField);
				if  (jsonCaption.has("confidence")) {
					captionText = captionText + " (confidence: "
						+ jsonCaption.getDouble("confidence") + ").";
				}
			}
		}
		return captionText;

	}
    
    private String getCaptionText(JSONObject jsonObj, String firstField, String secondField) {
		String captionText = null;
		if (jsonObj.has(firstField) && jsonObj.getJSONArray(firstField).length() > 0) {

			JSONObject jsonCaption = jsonObj.getJSONArray(firstField).getJSONObject(0);

			if (jsonCaption.has(secondField) && jsonCaption.has("confidence")) {
				captionText = "Caption: " + jsonCaption.getString(secondField) + " (confidence: "
						+ jsonCaption.getDouble("confidence") + ").";
			}
		}
		return captionText;
	}
    
    
    /**
     * Scales the given image to fit the label dimensions.
     * @param bImage: The image to fit.
     * @param label: The label to display the image.
     */
    private void scaleAndShowImage(BufferedImage bImage, JLabel label) {
        int bImageHeight = bImage.getHeight();
        int bImageWidth = bImage.getWidth();
        int labelHeight = label.getHeight();
        int labelWidth = label.getWidth();
        
        // Does this need to be scaled?
        if (labelHeight >= bImageHeight && labelWidth >= bImageWidth) {
            // If not, display the image and return.
            ImageIcon image = new ImageIcon(bImage);
            label.setIcon(image);
            return;
        }
        
        // Calculate the new width and height for the image.
        int newHeight;
        int newWidth;
        double bImageAspect = (double)bImageHeight / (double)bImageWidth;
        double labelAspect = (double)labelHeight / (double)labelWidth;
        
        if (bImageAspect > labelAspect) {
            newHeight = labelHeight;
            newWidth = (int)(((double)labelHeight / (double)bImageHeight) * (double)bImageWidth);
        } else {
            newWidth = labelWidth;
            newHeight = (int)(((double)labelWidth / (double)bImageWidth) * (double)bImageHeight);
        }
        
        // Create a new image scaled to the correct size.
        Image newImage = bImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        
        // Display the scaled image.
        ImageIcon labelImage = new ImageIcon(newImage);
        label.setIcon(labelImage);
        label.validate();
        label.repaint();
    }
    
    private void displayImage(String imageUrl) {
    	
   	     BufferedImage bImage = null;
         // Clear out the previous image, response, and caption, if any.
         sourceImage.setIcon(new ImageIcon());
         captionLabel.setText("");
         responseTextArea.setText("");

         // Display the image specified in the text box.
         try {
       	  if (ApiUtil.isHttpUrl(imageUrl)) {
       	  	bImage = ImageIO.read( new URL(imageUrl));
       	  } else {
       		  bImage = ImageIO.read(new FileInputStream(new File(imageUrl)));
       	  }
       	  scaleAndShowImage(bImage, sourceImage);
         } catch(IOException e) {
             responseTextArea.setText("Error loading image: " + e.getMessage());
             return;
         }
    }
     
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ComputerVisionApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ComputerVisionApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ComputerVisionApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ComputerVisionApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ComputerVisionApp().setVisible(true);
            }
        });
    }

    //API using default
    private ComputerVisionWrapper api = new  ComputerVisionWrapper( new ComputerVisionAPI()) ;
    
    //UI components 
    private JLabel captionLabel;
    private JLabel sourceImage;
    private JButton fileBrowseButton;
    private JButton analyzeImageButton;
    private JLabel imagePromptLabel;
    private JTextField imageURLTextField;
    private JLabel instructionLabel;
    private JPanel analyzePanel;
    private JLabel responseLabel;
    private JTextArea responseTextArea;
    private JScrollPane responseScrollPane;
    private JLabel sourceImageLabel;
    private JLabel titleLabel;

    private JTabbedPane tabbedPane;
    private JLabel thumbnailImage;
    private JLabel thumbnailLabel;
    private JComboBox<String> apiComboBox;
    
    private JLabel sKeyLabel;
    private JTextField sKeyTextField;
    private JComboBox<String> sRegionComboBox;
    private JLabel sRegionLabel;
    private JButton setSubscriptionButton;
    private JButton resetDefaultButton;
    private JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    private File ifcLastPath;
}
