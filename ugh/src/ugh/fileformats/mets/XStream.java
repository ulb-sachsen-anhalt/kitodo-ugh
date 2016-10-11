package ugh.fileformats.mets;

/*******************************************************************************
 * ugh.fileformats.mets / XStream.java
 *
 * Copyright 2010 Center for Retrospective Digitization, Göttingen (GDZ)
 *
 * http://gdz.sub.uni-goettingen.de
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 *
 * This Library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import ugh.dl.DigitalDocument;
import ugh.dl.Prefs;
import ugh.exceptions.PreferencesException;
import ugh.exceptions.ReadException;
import ugh.exceptions.WriteException;

/*******************************************************************************
 * @author Stefan Funk
 * @version 2010-02-15
 * @since 2008-10-30
 *
 *        TODOLOG
 *
 *        TODO Must we check the given Prefs object in the constructor?
 *
 ******************************************************************************/

public class XStream implements ugh.dl.Fileformat {

	/***************************************************************************
	 * VERSION STRING
	 **************************************************************************/

	private static final String	VERSION	= "1.2-20100215";

	/***************************************************************************
	 * STATIC FINALS
	 **************************************************************************/

	private static final Logger	LOGGER	= Logger
												.getLogger(ugh.dl.DigitalDocument.class);

	private DigitalDocument		digdoc	= null;
	private Prefs				myPreferences;

	/***************************************************************************
	 * <p>
	 * Default Constructor.
	 * </p>
	 *
	 * @throws PreferencesException
	 **************************************************************************/
	public XStream(Prefs thePrefs) throws PreferencesException {
		this.myPreferences = thePrefs;

		LOGGER.info(this.getClass().getName() + " " + getVersion());
	}

	/*
	 * Read the DigitalDocument from XStream XML.
	 *
	 * (non-Javadoc)
	 *
	 * @see ugh.dl.Fileformat#read(java.lang.String)
	 */
	public boolean read(String filename) throws ugh.exceptions.ReadException {

		LOGGER.info("Reading XStream");

		try {
			this.digdoc = new DigitalDocument().readXStreamXml(filename,
					this.myPreferences);
		} catch (FileNotFoundException e) {
			String message = "Can't find file '" + filename + "'!";
			LOGGER.error(message, e);
			throw new ReadException(message, e);
		} catch (UnsupportedEncodingException e) {
			String message = "Can't read file '" + filename
					+ "' because of wrong encoding!";
			LOGGER.error(message, e);
			throw new ReadException(message, e);
		}

		LOGGER
				.info("Sorting metadata according to occurrence in the Preferences");

		this.digdoc.sortMetadataRecursively(this.myPreferences);

		LOGGER.info("Reading XStream complete");

		return true;
	}

	/*
	 * Write the DigitalDocument to XStream XML.
	 *
	 * (non-Javadoc)
	 *
	 * @see ugh.fileformats.mets.MetsModsGdz#write(java.lang.String)
	 */
	@Deprecated
	public boolean write(String filename) throws WriteException {

		LOGGER.info("Writing XStream");

		try {
			this.digdoc.writeXStreamXml(filename);
		} catch (FileNotFoundException e) {
			String message = "Can't find file '" + filename + "'!";
			LOGGER.error(message, e);
			throw new WriteException(message, e);
		} catch (UnsupportedEncodingException e) {
			String message = "Can't write file '" + filename
					+ "' because of wrong encoding!";
			LOGGER.error(message, e);
			throw new WriteException(message, e);
		}

		LOGGER.info("Writing XStream complete");

		return true;
	}

	/***************************************************************************
	 * @return
	 **************************************************************************/
	public static String getVersion() {
		return VERSION;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ugh.dl.Fileformat#GetDigitalDocument()
	 */
	public DigitalDocument getDigitalDocument() {
		return this.digdoc;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ugh.dl.Fileformat#Update(java.lang.String)
	 */
	@Deprecated
	public boolean update(String filename) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ugh.dl.Fileformat#SetDigitalDocument(ugh.dl.DigitalDocument)
	 */
	public boolean setDigitalDocument(DigitalDocument inDoc) {
		this.digdoc = inDoc;

		return false;
	}

}
