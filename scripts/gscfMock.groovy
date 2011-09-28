import org.mbte.gretty.httpserver.*

@GrabResolver(name = 'gretty', root = 'http://groovypp.artifactoryonline.com/groovypp/libs-releases-local')
@Grab('org.mbte.groovypp:gretty:0.4.279')

GrettyServer server = []
server.groovy =
	[
		localAddress: new InetSocketAddress("localhost", 8080),

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
							strResponse = '''[
{"studyToken":"0082669f-2969-4ba8-9d38-3606bdd59cf5","version":2},
{"studyToken":"7bbba0fb-821e-4e16-903a-3887e1b54e59","version":2}]'''
							break
						case 'gscf/rest/getStudies':
							strResponse = '''[
{"studyToken":"0082669f-2969-4ba8-9d38-3606bdd59cf5","public":false,"title":"NuGO PPS human study","description":"Human study performed at RRI; centres involved: RRI, IFR, TUM, Maastricht U.","code":"PPSH","startDate":"2008-01-13T23:00:00Z","published":false,"Objectives":null,"Consortium":null,"Cohort name":null,"Lab id":null,"Institute":null,"Study protocol":null,"version":2},
{"studyToken":"7bbba0fb-821e-4e16-903a-3887e1b54e59","public":false,"title":"NuGO PPS3 mouse study leptin module","description":"C57Bl/6 mice were fed a high fat (45 en%) or low fat (10 en%) diet after a four week run-in on low fat diet.","code":"PPS3_leptin_module","startDate":"2008-01-01T23:00:00Z","published":false,"Objectives":null,"Consortium":null,"Cohort name":null,"Lab id":null,"Institute":null,"Study protocol":null,"version":2}]'''
							break
						case 'gscf/rest/getAuthorizationLevel':
							strResponse = '{"isOwner":false,"canRead":true,"canWrite":true}'
							break
						case 'gscf/rest/getAssays':

							println 'Get Assays'
							println request.parameters.studyToken

							switch (request.parameters.studyToken) {
								case '0082669f-2969-4ba8-9d38-3606bdd59cf5':
									strResponse = '''[
{"assayToken":"2be22ca0-8681-4614-985c-9b00ad603003","name":"Lipidomics profile after","module":{"class":"org.dbnp.gdt.AssayModule","id":2,"name":"Metabolomics module","notify":false,"openInFrame":true,"url":"http://localhost:8083/metabolomicsModule"},"Description":null,"Spectrometry technique":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":25,"name":"GC/MS","parent":{"class":"TemplateField","id":88}},"parentStudyToken":"0082669f-2969-4ba8-9d38-3606bdd59cf5"},
{"assayToken":"a2495ea3-9143-4d22-a1e0-f4ae2905c986","name":"Lipidomics profile before","module":{"class":"org.dbnp.gdt.AssayModule","id":2,"name":"Metabolomics module","notify":false,"openInFrame":true,"url":"http://localhost:8083/metabolomicsModule"},"Description":null,"Spectrometry technique":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":25,"name":"GC/MS","parent":{"class":"TemplateField","id":88}},"parentStudyToken":"0082669f-2969-4ba8-9d38-3606bdd59cf5"}]'''
									break
								case '7bbba0fb-821e-4e16-903a-3887e1b54e59':
									strResponse = '''[
{"assayToken":"bbe75294-e488-468f-be98-13e9469b261d","name":"Lipidomics profile","module":{"class":"org.dbnp.gdt.AssayModule","id":2,"name":"Metabolomics module","notify":false,"openInFrame":true,"url":"http://localhost:8083/metabolomicsModule"},"Description":null,"Spectrometry technique":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":26,"name":"LC/MS","parent":{"class":"TemplateField","id":88}},"parentStudyToken":"7bbba0fb-821e-4e16-903a-3887e1b54e59"}]'''
									break
							}
							break
						case 'gscf/rest/getSamples':

							println 'Get Samples'
							println request.parameters.assayToken

							switch (request.parameters.assayToken) {
								case '2be22ca0-8681-4614-985c-9b00ad603003':
									strResponse = new File('static/assay1_samples').text
									break
								case 'a2495ea3-9143-4d22-a1e0-f4ae2905c986':
									strResponse = new File('static/assay2_samples').text
									break
								case 'bbe75294-e488-468f-be98-13e9469b261d':
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
