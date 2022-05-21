import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import filter.FlipHorizontalFilter;
import pixel.PixelImage;

public class Main {


	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try {


			// Check for subfolders.
			File[] srcSubFolders = srcFolder.listFiles();
			if (srcSubFolders.length == 0) {
				// Exit if no subfolder.
				log(srcFolder.getAbsolutePath() + " is empty.");
				System.exit(0);
			}

			// Choose a subfolder randomly.
			File srcSubFolder = null;
			srcSubFolder = srcSubFolders[new Random().nextInt(srcSubFolders.length)];
			if (!srcSubFolder.isDirectory()) {
				// Exit if chosen subfolder is invalid.
				log(srcSubFolder.getAbsolutePath() + " is invalid.");
				System.exit(0);
			}

			// Search files/folders from selected subfolder.
			for (File srcGroup : srcSubFolder.listFiles()) {
				if (srcGroup.isFile()) {
					// Copy file directly to destination.
					log("Copying " + srcGroup.getAbsolutePath() + " -> " + destFolder.getAbsolutePath());
					Files.copy(srcGroup.toPath(), Paths.get(destFolder.getAbsolutePath() + "\\" + srcGroup.getName()));
				} else if (srcGroup.isDirectory()) {
					// Copy a random file from group folder to destination.
					File[] srcFiles = srcGroup.listFiles();
					File srcFile = srcFiles[new Random().nextInt(srcFiles.length)];
					log("Copying " + srcFile.getAbsolutePath() + " -> " + destFolder.getAbsolutePath());
					String srcFileName = srcFile.getName();
					String destFileName = destFolder.getAbsolutePath() + "\\" + srcFileName;
					Files.copy(srcFile.toPath(), Paths.get(destFileName));

					// Randomly flip image.
					if (new Random().nextInt(2) == 0) {
						log("Flipping " + destFileName);
						File destFile = new File(destFileName);
						PixelImage img = PixelImage.load(destFile);
						new FlipHorizontalFilter().filter(img);
						String srcFileExt = srcFileName.substring(srcFileName.lastIndexOf('.') + 1);
						destFile.delete();
						img.save(new File(destFileName),srcFileExt);
					}

					// Shuffle group folder.
					File newSrcGrp = new File(srcSubFolder.getAbsolutePath() + "\\"
							+ (char) (new Random().nextInt(26) + 'A') + System.currentTimeMillis());
					log("Renaming " + srcGroup.getAbsolutePath() + " -> " + newSrcGrp.getAbsolutePath());
					srcGroup.renameTo(newSrcGrp);
					Thread.sleep(2);
				}
			}

			// Shuffle source subfolder.
			File newSrcSubDir = new File(srcFolder.getAbsolutePath() + "\\" + Instant.now().getEpochSecond());
			log("Renaming " + srcSubFolder.getAbsolutePath() + " -> " + newSrcSubDir.getAbsolutePath());
			srcSubFolder.renameTo(newSrcSubDir);

			// Save properties file.
			PROP.store(new FileOutputStream(PROP_FILE_NAME), null);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), JOptionPane.ERROR_MESSAGE);
			throw e;
		}
	}




}
