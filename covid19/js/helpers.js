function millisecondsSince(date) { 
	return date ? Date.now() - date : null }

function secondsSince(date) {
	let ms = millisecondsSince(date)
	return ms ? ms / 1000.0 : null
}

function minutesSince(date) {
	let s = secondsSince(date) 
	return s ? s / 60.0 : null
}

function hoursSince(date) {
	let m = minutesSince(date) 
	return m ? m / 60.0 : null
}

function daysSince(date) {
	let h = hoursSince(date)
	return h ? h / 24.0 : null
}

async function unzip(zipString) {
	// - create a TextReader object with the zipped content 
	// - create a ZipReader object to read the zipped data
	const zipReader = new zip.ZipReader(new zip.TextReader(zipString));

	// - get entries from the zip file
	const entries = await zipReader.getEntries();

	// - use a TextWriter object to write the unzipped data of the first entry into a string
	const data = await entries[0].getData(new zip.TextWriter());
	// - close the ZipReader object
	await zipReader.close();

	return JSON.parse(data);
}