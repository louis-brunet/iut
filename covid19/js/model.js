const summaryURL = 'https://api.covid19api.com/summary'
const detailsURL = 'https://api.covid19api.com/export'
const countryDetailsURL = 'https://api.covid19api.com/total/dayone/country/' // + country slug
const basePath = window.location.pathname.replace('#', '') //new URL(baseURL).pathname
const globalCountryCode = 'GLOBAL'
const hourLength = 1000 * 60 * 60 // milliseconds
const dayLength = 24 * hourLength // milliseconds
const weekLength = 7 * dayLength // milliseconds
const dataExpiration = 10 *hourLength // milliseconds 
const dataAveragePeriod = 6 * dayLength // milliseconds
const graphMovingAverageDayCountInEachDirection = 3 // days (index in daily values array)
const unavailableErrorMessage = 'API covid19api est indisponible. Réessayez plus tard.'
const tables = {
	fetchLog : {
		name: 'fetchLog',
		columns : [  'date', 'tableName' ]
	},
	country : {
		name: 'country',
		columns : [ 'name', 'slug', 'countryCode' ]
	},
	/*location : {
		name: 'location',
		columns: [ 'locationId', 'countryCode' ]
	},*/
	summary : {
		name : 'summary',
		columns : [ 
			'countryCode', 'name', 
			'newConfirmed', 'newDeaths', 'newRecovered', 
			'totalConfirmed', 'totalDeaths', 'totalRecovered' 
		]
	},
	/*details : {
		name : 'details',
		columns : [ 'locationId', 'countryCode', 'date', 'confirmed', 'recovered', 'deaths' ]
	},*/
	countryDailyTotals : {
		name : 'countryDailyTotals',
		columns : [ 'countryCode', 'date', 'totalConfirmed', 'totalRecovered', 'totalDeaths' ]
	}
}

class Model {
	constructor() {
		this.db = new localStorageDB('covid19', localStorage)

		if (this.db.isNew()) {
			this.createTables()
			console.log('tables created')
			this.db.commit();
		}

		// La fonction donnée à la table pour mettre à jour ses données
		this.tableQuery = (sortColumn, sortOrder, pageSize, pageNumber, filterColumn, filterText = '') => {
			console.log('tableQuery args = ', [sortColumn, sortOrder, pageSize, pageNumber, filterColumn, filterText]);
			let sortOption = []
			if (sortColumn !== 'name') sortOption.push(['name', 'ASC'])
			sortOption.push([sortColumn, sortOrder])

			let options = {
				sort : sortOption, 
				query : function(row) {
					return (!filterText || row[filterColumn].toLowerCase().includes(filterText.toLowerCase()))
						&& (row.countryCode !== globalCountryCode);
				}
			}
	
			let queryResult = this.db.queryAll(tables.summary.name, options)
			let start = (pageNumber - 1) * pageSize;
			let page = queryResult.slice(start, start + pageSize)

			let result = { totalRows : queryResult.length };
			result.rows = page.map( row => {
				let arr = [];
				
				arr.push({
					value: row.name,
					link: `${ basePath }#country/${ row.countryCode }`,
					column: 'name'
				},{
					value: row.newConfirmed,
					column: 'newConfirmed',
					class: 'confirmed'
				},{
					value: row.newRecovered,
					column: 'newRecovered',
					class: 'recovered'
				},{
					value: row.newDeaths,
					column: 'newDeaths',
					class: 'deaths'
				},{
					value: row.totalConfirmed,
					column: 'totalConfirmed',
					class: 'confirmed'
				},{
					value: row.totalRecovered,
					column: 'totalRecovered',
					class: 'recovered'
				},{
					value: row.totalDeaths,
					column: 'totalDeaths',
					class: 'deaths'
				});

				return arr;
			})

			return result;
		}

		// La fonction donnée au graphe pour mettre à jour ses données
		this.countryQuery = async (countryCode) => {
			let graphValues = 
				this.countryDailyDetailsToMovingAverages(
				//this.countryDailyDetailsToAverages(
				await this.getCountryDailyDetails(countryCode), 
				graphMovingAverageDayCountInEachDirection)
				//dataAveragePeriod) 
			let q = this.db.queryAll(tables.country.name, {
				query: { countryCode: countryCode },
				limit: 1
			})
			let res = {
				code: countryCode,
				name: q.length > 0 ? q[0].name : undefined,
				graphs: [
					this.createGraphData('Cumul', 'cumulative', graphValues),
					this.createGraphData('Différentiel', 'differential', graphValues)
				]
			}

			console.log('countryQuery returned : ', res)
			return res
		}
	}

	createTables() {
		for (const table in tables) {
			if (Object.hasOwnProperty.call(tables, table)) {
				const t = tables[table];
				this.db.createTable(t.name, t.columns)
				console.log(`Created table ${t.name}`);
			}
		}
	}

	createGraphData(title, type, values) {
		const xMinorStep =  7 * dayLength
		const xMajorStep =  28 * dayLength

		return {
			graphTitle: title,
			graphType: type,
			values: values,
			xAxis: {
				id: 'date', 
				name: 'Date', 
				minorStep: xMinorStep, 
				majorStep: xMajorStep,
				
				valueToString(v) { 
					let d = new Date(v)
					return d.getDate()+' '+d.toLocaleString('default', { month: 'short' })
				}
			}, 
			yAxes: [{id: 'totalConfirmed', name: 'Cas', color: 'blue'},
					{id: 'totalRecovered', name: 'Guéris', color: 'green'},
					{id: 'totalDeaths', name: 'Morts', color: 'red'}],
			yMajorLineCount: 4,
			yMinorLineCount: 16,
			axisColor: '#333',
			majorLineColor: '#bbb',
			minorLineColor: '#ddd',
			labelColor: '#666',
			graphBackground: '#eef',
			axisPaddingHorizontal: 80, // pixels
			axisPaddingVertical: 64, // pixels
			labelFont: '17px sans-serif'
		}
	}

	async updateDB() {
		console.log('Updating DB...')
		
		let start = Date.now()
		let data = await this.getNewData()
		console.log(`received new data @ ${secondsSince(start)}s :`); console.log(data)
		
		if (data) {
			if (data.summary) {
				this.updateCountryAndSummaryTables(data.summary.Global, data.summary.Countries)
				console.log(`updated countries & summaries @ ${secondsSince(start)}s`)
				//this.logFetch('summary')
			}
			/*if (data.details)  {
				this.updateDetailsAndLocationTables(data.details)
				console.log(`updated locations & all details @ ${secondsSince(start)}s`)
			}*/


			//this.logFetch();

			console.log('DB updated')
			this.db.commit()
			console.log('changes committed')
		} else		
			console.log('DB not updated, no commit')
	}

	updateCountryAndSummaryTables(globalData, countriesData) {
		this.db.insertOrUpdate(
			tables.country.name, 
			{ countryCode: globalCountryCode}, 
			{
				countryCode: globalCountryCode,
				name: 'Global',
				slug: 'global'
			}
		);
		this.db.insertOrUpdate(
			tables.summary.name, 
			{ countryCode : globalCountryCode },
			{
				countryCode : globalCountryCode,
				name : 'Global',
				newConfirmed : globalData.NewConfirmed,
				newDeaths : globalData.NewDeaths,
				newRecovered : globalData.NewRecovered,
				totalConfirmed : globalData.TotalConfirmed,
				totalDeaths : globalData.TotalDeaths,
				totalRecovered : globalData.TotalRecovered
			}
		);

		for(const country of countriesData) {
			this.db.insertOrUpdate(
				tables.country.name,
				{ countryCode : country.CountryCode },
				{ 
					countryCode : country.CountryCode,
					name : country.Country,
					slug : country.Slug
				}
			);
			this.db.insertOrUpdate(
				tables.summary.name,
				{ countryCode : country.CountryCode },
				{
					countryCode : country.CountryCode,
					name : country.Country,
					newConfirmed : country.NewConfirmed,
					newDeaths : country.NewDeaths,
					newRecovered : country.NewRecovered,
					totalConfirmed : country.TotalConfirmed,
					totalDeaths : country.TotalDeaths,
					totalRecovered : country.TotalRecovered
				}
			)
		}

		this.logFetch(tables.country.name)
		this.logFetch(tables.summary.name)
	}

	/*updateDetailsAndLocationTables(details) {
		let loggedExample = false;

		for (const dailyData of details) {
			if (!  loggedExample)  {
				console.log('Daily detail format : ', dailyData);
				loggedExample = true;
			}

			let date = Date.parse(dailyData.Date)
			let locationId = `${dailyData.CountryCode}/${dailyData.Province}/${dailyData.City}/${dailyData.CityCode}` 

			this.db.insertOrUpdate(
				tables.details.name,
				{ locationId: locationId, date: date },
				{ 
					locationId: locationId,
					countryCode: dailyData.CountryCode,
					date: date,
					confirmed: dailyData.Confirmed,
					deaths: dailyData.Deaths,
					recovered: dailyData.Recovered
				}
			)

			this.db.insertOrUpdate(
				tables.location.name,
				{ locationId: locationId },
				{ 
					locationId: locationId, // 
					countryCode: dailyData.CountryCode
				}
			)
		}

		this.logFetch(tables.details.name)
		this.logFetch(tables.location.name)
	}*/
	
	logFetch(tableName) {
		if (this.db.tableExists(tableName)) {
			this.db.insert(tables.fetchLog.name, { tableName: tableName,  date : Date.now() });
		} else throw `Table does not exist : ${tableName}`
	}

	getLastFetchDateMilliseconds(tableName) {
		if (this.db.tableExists(tableName)) {
			let res = this.db.queryAll(tables.fetchLog.name, { 
				query : { tableName: tableName },
				limit : 1, 
				sort : [['date', 'DESC']] 
			});
			return res.length > 0 ? res[0].date : null;
		} else throw `Table does not exist  : ${tableName}`
	}

	eraseLastFetchLog(tableName) {
		if (this.db.tableExists(tableName)) {
			this.db.deleteRows(tables.fetchLog.name, {date: this.getLastFetchDateMilliseconds(tableName)});
		} else throw `Table does not exist  : ${tableName}`
	}

	// retrieve data to update DB 
	async getNewData() {
		let data = { 
			summary : null/*, 
			details : null */
		};

		let summaryTableFetchDate = this.getLastFetchDateMilliseconds('summary')
//		let detailsTableFetchDate = this.getLastFetchDateMilliseconds('details')

		let needToFetchSummary = ( ! summaryTableFetchDate || millisecondsSince(summaryTableFetchDate) > dataExpiration ) 
//		let needToFetchDetails = ( ! detailsTableFetchDate || millisecondsSince(detailsTableFetchDate) > dataExpiration )

		// Summary
		if ( needToFetchSummary ) {
			let stored = JSON.parse(localStorage.getItem('summary'))

			if ( ! stored || ! stored.fetchDate || millisecondsSince(stored.fetchDate) > dataExpiration ) {
				let promise = new Promise((resolve, reject) => {
					let xhr = new XMLHttpRequest()
					xhr.open('GET',summaryURL)
					xhr.withCredentials = true
					xhr.responseType = 'json'
					xhr.onload = () => {
						let r = xhr.response
						r.fetchDate = Date.now()
						localStorage.setItem('summary', JSON.stringify(r))
						console.log('fetched summary from API: ', r)
						resolve(r)
					};
					xhr.onerror = () => {
						console.warn(`XHR error for ${summaryURL} : ${ xhr.statusText }`)
						alert( unavailableErrorMessage )
						reject(null)
					};
					xhr.send()
				});
				
				let res = await promise
				if (res !== null) data.summary = res
			} else {
				data.summary = stored
				console.log('fetched summary from localStorage: ', data.summary)
			}
		}  else 
			console.log('No update required on summary table')
/*
		//// Details
		if ( needToFetchDetails ) {
			let promise = new Promise((resolve, reject) => {
				let xhr = new XMLHttpRequest()
				xhr.open('GET', detailsURL)
				xhr.withCredentials = true
				xhr.responseType = 'blob'
				//xhr.responseType = 'json';
				xhr.onload = async () => {
					//console.log(`received /export zip file : `)
					//console.log(xhr.response);
					let res = await unzip(xhr.response)

					res.fetchDate = Date.now()
					//localStorage.setItem('details', JSON.stringify(res));
					console.log('fetched all details from API: ', res)
					resolve(res)
				};
				xhr.onerror = () => {
					console.warn(`XHR error for ${detailsURL} : ${ xhr.statusText }`)
					alert(unavailableErrorMessage)
					reject(null)
				};
				xhr.send()
			});
			let res = await promise
			if (res !== null) data.details = res
			//promise.then( res => { if (res !== null) data.details = res; } );

		} else 
			console.log('No update required on details table');
*/
		if ( !data.summary /*&& !data.details*/) 
			return null
		
		return data
	}

	async getCountryDailyDetails(countryCode) {
		let lastDay = this.getLastDetailDateFor(countryCode)
		if ( !lastDay || daysSince(lastDay) > 3 ) {
			console.log(`fetching remote details for ${ countryCode }, hours since last detail date = ${ hoursSince(lastDay) }h`);
			let details = await this.fetchRemoteCountryDetails(countryCode)		
			let insertCount = 0;
			for(const day of details) {
				let date = Date.parse(day.Date)
				if ( !lastDay || date > lastDay ) {
					this.db.insertOrUpdate(
						tables.countryDailyTotals.name,
						{ countryCode: countryCode, date: date },
						{ 
							countryCode: countryCode, 
							date: date,
							totalConfirmed: day.Confirmed,
							totalDeaths: day.Deaths,
							totalRecovered: day.Recovered
						}

					)
					insertCount ++
				}
			}
			this.db.commit()
			console.log(`fetched remote details for ${ countryCode }, inserted ${ insertCount } rows, hours since last detail date = ${ hoursSince(this.getLastDetailDateFor(countryCode)) }h`);
		}  else console.log(`country daily details for ${countryCode} were not updated`)
		
		return this.db.queryAll(tables.countryDailyTotals.name, { 
			query: { countryCode: countryCode },
			sort: [['date', 'ASC']]
		})
	}

	countryDailyDetailsToAverages(daily, periodLength) {
		let res = []
		//let weeklyDetail = {}
		let currentWeek = null
		let dayCount = 0

		for (let i = 0; i < daily.length; i++, dayCount++) {
			const dailyValue = daily[i]

			if (!currentWeek || (dailyValue.date - currentWeek.date > periodLength) ) {
				if (currentWeek && (dayCount !== 0)) {
					currentWeek.totalConfirmed = currentWeek.totalConfirmed / dayCount
					currentWeek.totalDeaths = currentWeek.totalDeaths / dayCount
					currentWeek.totalRecovered = currentWeek.totalRecovered / dayCount
					res.push(currentWeek)
					//dayCount = 0;
				}

				currentWeek = {
					countryCode: dailyValue.countryCode,
					date: dailyValue.date,
					totalConfirmed: dailyValue.totalConfirmed,
					totalDeaths: dailyValue.totalDeaths,
					totalRecovered: dailyValue.totalRecovered
				}
				dayCount = 0
			} else {
				currentWeek.totalConfirmed += dailyValue.totalConfirmed
				currentWeek.totalDeaths += dailyValue.totalDeaths
				currentWeek.totalRecovered += dailyValue.totalRecovered
			}
			

		}

		return res
	}

	countryDailyDetailsToMovingAverages(daily, indexCountToLookBothWays) {
		let res = []

		for (let i = 0; i < daily.length; i++) {
			res.push(this.getMovingAverages(daily, i, indexCountToLookBothWays));
		}

		return res
	}

	getMovingAverages(dailyData, startIndex, indexCountToLookBothWays) {
		let valueCount = 0
		let averages = {
			countryCode: dailyData[startIndex].countryCode,
			date: dailyData[startIndex].date,
			totalConfirmed : 0,
			totalDeaths : 0,
			totalRecovered : 0
		}
		for (let i = startIndex - indexCountToLookBothWays; i <= startIndex + indexCountToLookBothWays; i++) {
			if (i >= 0 && i < dailyData.length) {
				valueCount++
				averages.totalConfirmed += dailyData[i].totalConfirmed
				averages.totalDeaths += dailyData[i].totalDeaths
				averages.totalRecovered += dailyData[i].totalRecovered
			}
			
		}

		averages.totalConfirmed = averages.totalConfirmed / valueCount
		averages.totalDeaths = averages.totalDeaths / valueCount
		averages.totalRecovered = averages.totalRecovered / valueCount

		return averages;
	}

	getLastDetailDateFor(countryCode) {
		let r = this.db.queryAll(tables.countryDailyTotals.name, {
			query: {countryCode: countryCode},
			sort: [['date', 'DESC']],
			limit: 1
		})

		return r.length > 0 ? r[0].date : null;
	}

	async fetchRemoteCountryDetails(countryCode) {
		let slug = this.codeToSlug(countryCode)
		let url = countryDetailsURL + slug;
		let promise = new Promise((resolve, reject) => {
			let xhr = new XMLHttpRequest();
			xhr.open('GET', url);
			xhr.withCredentials = true;
			xhr.responseType = 'json'

			xhr.onload = async () => {
				console.log(`fetched ${ slug } details from API: `, xhr.response);
				resolve(xhr.response);
			};
			xhr.onerror = () => {
				console.warn(`XHR error for ${ url } : ${ xhr.statusText }`);
				alert(unavailableErrorMessage)
				reject( null );
			};
			xhr.send();
		})

		return await promise;
	}

	// throws error if not found
	codeToSlug(countryCode) {
		let r = this.db.queryAll(
			tables.country.name,
			{ 
				query : { countryCode: countryCode },
				limit : 1 
		 	}
		)

		if (r.length === 0) 
			throw `Country not found : code ${countryCode}`
		
		return r[0].slug
	}

	getGlobalSummary() {
		let r = this.db.queryAll(tables.summary.name, {
			query: { countryCode: globalCountryCode },
			limit: 1
		})

		return r.length > 0 ? r[0] : null;
	}
	
}


const model = new Model()


