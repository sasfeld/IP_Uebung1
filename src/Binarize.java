// Copyright (C) 2014 by Klaus Jung
// All rights reserved.
// Date: 2014-10-02

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.*;
import java.io.File;

public class Binarize extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static final int border = 10;
	private static final int maxWidth = 400;
	private static final int maxHeight = 400;
	private static final File openPath = new File(".");
	private static final String title = "Binarisierung";
	private static final String author = "MeinName";	// TODO: type in your name here
	private static final String initalOpen = "tools1.png";
	
	private static JFrame frame;
	
	private ImageView srcView;				// source image view
	private ImageView dstView;				// binarized image view
	
	private JComboBox<String> methodList;	// the selected binarization method
	private JLabel statusLine;				// to print some status text

	public Binarize() {
        super(new BorderLayout(border, border));
        
        // load the default image
        File input = new File(initalOpen);
        
        if(!input.canRead()) input = openFile(); // file not found, choose another image
        
        srcView = new ImageView(input);
        srcView.setMaxSize(new Dimension(maxWidth, maxHeight));
       
		// create an empty destination image
		dstView = new ImageView(srcView.getImgWidth(), srcView.getImgHeight());
		dstView.setMaxSize(new Dimension(maxWidth, maxHeight));
		
		// load image button
        JButton load = new JButton("Bild �ffnen");
        load.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		File input = openFile();
        		if(input != null) {
	        		srcView.loadImage(input);
	        		srcView.setMaxSize(new Dimension(maxWidth, maxHeight));
	                binarizeImage();
        		}
        	}        	
        });
         
        // selector for the binarization method
        JLabel methodText = new JLabel("Methode:");
        String[] methodNames = {"50% Schwellwert", "Iso-Data-Algorithmus"};
        
        methodList = new JComboBox<String>(methodNames);
        methodList.setSelectedIndex(0);		// set initial method
        methodList.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                binarizeImage();
        	}
        });
        
        // slider for threshold
        JSlider thresholdSlider = new JSlider(0, 255, 128);
        
        
        // some status text
        statusLine = new JLabel(" ");
        
        // arrange all controls
        JPanel controls = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,border,0,0);
        controls.add(load, c);
        controls.add(methodText, c);
        controls.add(methodList, c);
        
        JPanel images = new JPanel(new FlowLayout());
        images.add(srcView);
        images.add(dstView);
        
        add(controls, BorderLayout.NORTH);
        add(images, BorderLayout.CENTER);
        add(statusLine, BorderLayout.SOUTH);
               
        setBorder(BorderFactory.createEmptyBorder(border,border,border,border));
        
        // perform the initial binarization
        binarizeImage();
	}
	
	private File openFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (*.jpg, *.png, *.gif)", "jpg", "png", "gif");
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(openPath);
        int ret = chooser.showOpenDialog(this);
        if(ret == JFileChooser.APPROVE_OPTION) {
    		frame.setTitle(title + chooser.getSelectedFile().getName());
        	return chooser.getSelectedFile();
        }
        return null;		
	}
	
	private static void createAndShowGUI() {
		// create and setup the window
		frame = new JFrame(title + " - " + author + " - " + initalOpen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JComponent newContentPane = new Binarize();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        // display the window.
        frame.pack();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        frame.setLocation((screenSize.width - frame.getWidth()) / 2, (screenSize.height - frame.getHeight()) / 2);
        frame.setVisible(true);
	}

	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
	
	
    protected void binarizeImage() {
  
        String methodName = (String)methodList.getSelectedItem();
        
        // image dimensions
        int width = srcView.getImgWidth();
        int height = srcView.getImgHeight();
    	
    	// get pixels arrays
    	int srcPixels[] = srcView.getPixels();
    	int dstPixels[] = java.util.Arrays.copyOf(srcPixels, srcPixels.length);
    	
    	String message = "Binarisieren mit \"" + methodName + "\"";

    	statusLine.setText(message);

		long startTime = System.currentTimeMillis();
		
    	switch(methodList.getSelectedIndex()) {
    	case 0:	// 50% Schwellwert
    		binarize50(dstPixels);
    		break;
    	case 1:	// ISO-Data-Algorithmus
    		java.util.Arrays.fill(dstPixels, 0xffffffff);
    		break;
    	}

		long time = System.currentTimeMillis() - startTime;
		   	
        dstView.setPixels(dstPixels, width, height);
        
        //dstView.saveImage("out.png");
    	
        frame.pack();
        
    	statusLine.setText(message + " in " + time + " ms");
    }
    
    void binarize50(int pixels[]) {
    	for(int i = 0; i < pixels.length; i++) {
    		int gray = ((pixels[i] & 0xff) + ((pixels[i] & 0xff00) >> 8) + ((pixels[i] & 0xff0000) >> 16)) / 3;
    		pixels[i] = gray < 128 ? 0xff000000 : 0xffffffff;
    	}
    }
    

}
    