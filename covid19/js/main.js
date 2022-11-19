main()

async function main() {
	await riot.compile()
	await model.updateDB()

	let data = {
		basePath : basePath,
		global : model.getGlobalSummary(),
		tableData : {
			pageSizes : [10,20,50,100,200],
			defaultPageSizeIndex : 1,
			columns : [
				{name : 'Pays', weight : 3, columnId : 'name'},
				{name : 'Cas', weight : 1, class : 'confirmed', columnId : 'newConfirmed'},
				{name : 'Guéris', weight: 1, class : 'recovered', columnId : 'newRecovered' },
				{name : 'Morts', weight: 1, class : 'deaths', columnId : 'newDeaths' },
				{name : 'Cas total', weight : 2, class : 'confirmed', columnId : 'totalConfirmed'},
				{name : 'Guéris total', weight: 2, class : 'recovered', columnId : 'totalRecovered' },
				{name : 'Morts total', weight: 2, class : 'deaths', columnId : 'totalDeaths' }
			],
			filterColumnIndex : 0,
			sortColumn : 'name',
			sortOrder : 'ASC',
			queryFunction : model.tableQuery //  (sortColumn, sortOrder, pageSize, pageNumber, filterColumn, filterText = '') => {}
		},
		countryData : {
			queryFunction : model.countryQuery // async (countryCode) => {}
		}
	}
	
	console.log('mounting app : ', riot.mount('my-app', data));
}