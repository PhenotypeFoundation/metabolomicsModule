import org.mbte.gretty.httpserver.*

@GrabResolver(name = 'gretty', root = 'http://groovypp.artifactoryonline.com/groovypp/libs-releases-local')
@Grab('org.mbte.groovypp:gretty:0.4.279')

GrettyServer server = []
server.groovy =
	[
		localAddress: new InetSocketAddress("localhost", 8080),
		//localAddress: new InetSocketAddress(InetAddress.localHost.canonicalHostName, 8080),

		defaultHandler:
		{
			response.redirect "/"
		},

		static: "./static",

		"/:name":
		{
			get
				{
					println request.parameters

					def strResponse = ''
					switch (request.parameters.name) {
						case 'gscf/rest/getUser':
							strResponse = '{"username":"admin","id":1,"isAdministrator":true}'
							break
						case 'gscf/rest/getStudyVersions':
							strResponse = '''[{"studyToken":"aaa","version":1},{"studyToken":"bbb","version":1}]'''
							break
						case 'gscf/rest/getStudies':
							strResponse = '''[
{"studyToken":"aaa","public":false,"title":"Study A","description":"Description A","code":"SA","startDate":"2008-01-13T23:00:00Z","published":false,"Objectives":null,"Consortium":null,"Cohort name":null,"Lab id":null,"Institute":null,"Study protocol":null,"version":1},
{"studyToken":"bbb","public":false,"title":"Study B","description":"Description B","code":"SB","startDate":"2008-01-01T23:00:00Z","published":false,"Objectives":null,"Consortium":null,"Cohort name":null,"Lab id":null,"Institute":null,"Study protocol":null,"version":1}]'''
							break
						case 'gscf/rest/getAuthorizationLevel':
							strResponse = '{"isOwner":false,"canRead":true,"canWrite":true}'
							break
						case 'gscf/rest/getAssays':

							println 'Get Assays'
							println request.parameters.studyToken

							switch (request.parameters.studyToken) {
								case 'aaa':
									strResponse = '''[
{"assayToken":"yyy","name":"Assay Y","module":{"class":"org.dbnp.gdt.AssayModule","id":2,"name":"Metabolomics module","notify":false,"openInFrame":true,"url":"http://localhost:8083/metabolomicsModule"},"Description":null,"Spectrometry technique":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":25,"name":"LC/MS","parent":{"class":"TemplateField","id":88}},"parentStudyToken":"aaa"},
{"assayToken":"zzz","name":"Assay Z","module":{"class":"org.dbnp.gdt.AssayModule","id":2,"name":"Metabolomics module","notify":false,"openInFrame":true,"url":"http://localhost:8083/metabolomicsModule"},"Description":null,"Spectrometry technique":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":25,"name":"GC/MS","parent":{"class":"TemplateField","id":88}},"parentStudyToken":"aaa"}]'''
									break
								case 'bbb':
									strResponse = '''[
{"assayToken":"xxx","name":"Assay X","module":{"class":"org.dbnp.gdt.AssayModule","id":2,"name":"Metabolomics module","notify":false,"openInFrame":true,"url":"http://localhost:8083/metabolomicsModule"},"Description":null,"Spectrometry technique":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":26,"name":"MS/MS","parent":{"class":"TemplateField","id":88}},"parentStudyToken":"bbb"}]'''
									break
							}
							break
						case 'gscf/rest/getSamples':

							println 'Get Samples'
							println request.parameters.assayToken

							switch (request.parameters.assayToken) {
								case 'xxx':
									strResponse = new File('static/assay2_samples').text
									break
								case 'yyy':
									strResponse = new File('static/assay1_samples').text
									break
								case 'zzz':
									strResponse = new File('static/assay3_samples').text
									break
							}
							break
						case 'gscf/login/auth_remote':
							response.redirect URLDecoder.decode(request.parameters.returnUrl)
							break
						case 'gscf/logout/remote':
							strResponse = 'Logged out ...'
							break
						case 'gscf/rest/isUser':
							strResponse = '{"authenticated":true}'
							break
						default: strResponse = 'unknown request: ' + request.parameters.name
					}

					response.html = strResponse

				} // end of get
		}
	]

server.start()
