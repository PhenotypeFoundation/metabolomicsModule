import org.mbte.gretty.httpserver.*

@GrabResolver(name = 'gretty', root = 'http://groovypp.artifactoryonline.com/groovypp/libs-releases-local')
@Grab('org.mbte.groovypp:gretty:0.4.279')

GrettyServer server = []
server.groovy =
	[
//		localAddress: new InetSocketAddress(InetAddress.localHost.canonicalHostName, 8080),
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
					def strResponse = ''
					switch (request.parameters.name) {
						case 'gscf/rest/getUser':
							strResponse = '{"username":"admin","id":1,"isAdministrator":true}'
							break
						case 'gscf/rest/getStudyVersions':
							strResponse = '''[
{"studyToken":"aaa","version":1}, 
{"studyToken":"bbb","version":1},
{"studyToken":"f79a2713-c9bb-4f1c-93a9-ddfcb125930a","version":1},
{"studyToken":"1962f9d0-77b3-415f-9857-81f00e6395b3","version":1},
{"studyToken":"cba83175-89dc-4a92-8680-99c90a6dd0b4","version":1}]'''
							break
						case 'gscf/rest/getStudies':
							strResponse = '''[
{"studyToken":"aaa","public":false,"title":"Study A","description":"Description A","code":"SA","startDate":"2008-01-13T23:00:00Z","published":false,"Objectives":null,"Consortium":null,"Cohort name":null,"Lab id":null,"Institute":null,"Study protocol":null,"version":1},
{"studyToken":"bbb","public":false,"title":"Study B","description":"Description B","code":"SB","startDate":"2008-01-01T23:00:00Z","published":false,"Objectives":null,"Consortium":null,"Cohort name":null,"Lab id":null,"Institute":null,"Study protocol":null,"version":1},
{"blob":null,"startDate":"2011-10-09T22:00:00Z","title":"Test Study for PLS galaxy tool","description":"NMR data for PLS regression, 36 subjects ","studyToken":"f79a2713-c9bb-4f1c-93a9-ddfcb125930a","code":null,"published":false,"public":false,"version":1},
{"startDate":"2008-01-13T23:00:00Z","Lab id":null,"studyToken":"1962f9d0-77b3-415f-9857-81f00e6395b3","Consortium":null,"code":"PPSH","Study protocol":null,"Cohort name":null,"version":1,"title":"NuGO PPS human study","description":"Human study performed at RRI; centres involved: RRI, IFR, TUM, Maastricht U.","Objectives":null,"public":false,"published":false,"Institute":null},
{"startDate":"2008-01-01T23:00:00Z","Lab id":null,"studyToken":"cba83175-89dc-4a92-8680-99c90a6dd0b4","Consortium":null,"code":"PPS3_leptin_module","Study protocol":null,"Cohort name":null,"version":1,"title":"NuGO PPS3 mouse study leptin module","description":"C57Bl/6 mice were fed a high fat (45 en%) or low fat (10 en%) diet after a four week run-in on low fat diet.","Objectives":null,"public":false,"published":false,"Institute":null}]'''
							break
						case 'gscf/rest/getAuthorizationLevel':
							strResponse = '{"isOwner":false,"canRead":true,"canWrite":true}'
							break
						case 'gscf/rest/getAssays':
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
								case 'f79a2713-c9bb-4f1c-93a9-ddfcb125930a':
									strResponse = '''[
{"module":{"id":54,"openInFrame":false,"notify":true,"name":"Metabolomics module","class":"org.dbnp.gdt.AssayModule","url":"http://localhost:8083/metabolomicsModule"},"parentStudyToken":"f79a2713-c9bb-4f1c-93a9-ddfcb125930a","assayToken":"32012ce2-ec1e-4e21-9852-a7f527757aaf","name":"NMR Urine Samples"}]'''
									break
								case '1962f9d0-77b3-415f-9857-81f00e6395b3':
									strResponse = '''[
{"Description":null,"module":{"id":2,"openInFrame":true,"notify":false,"name":"Metabolomics module","class":"org.dbnp.gdt.AssayModule","url":"http://localhost:8083/metabolomicsModule"},"parentStudyToken":"1962f9d0-77b3-415f-9857-81f00e6395b3","assayToken":"bc117e31-f66e-474a-9b55-26ca94add48b","Spectrometry technique":{"id":25,"name":"GC/MS","parent":{"id":88,"class":"TemplateField"},"class":"org.dbnp.gdt.TemplateFieldListItem"},"name":"Lipidomics profile after"},
{"Description":null,"module":{"id":2,"openInFrame":true,"notify":false,"name":"Metabolomics module","class":"org.dbnp.gdt.AssayModule","url":"http://localhost:8083/metabolomicsModule"},"parentStudyToken":"1962f9d0-77b3-415f-9857-81f00e6395b3","assayToken":"f80878f9-791a-4045-af01-c160bfdc7795","Spectrometry technique":{"id":25,"name":"GC/MS","parent":{"id":88,"class":"TemplateField"},"class":"org.dbnp.gdt.TemplateFieldListItem"},"name":"Lipidomics profile before"}]'''
									break
								case 'cba83175-89dc-4a92-8680-99c90a6dd0b4':
									strResponse = '''[
{"Description":null,"module":{"id":2,"openInFrame":true,"notify":false,"name":"Metabolomics module","class":"org.dbnp.gdt.AssayModule","url":"http://localhost:8083/metabolomicsModule"},"parentStudyToken":"cba83175-89dc-4a92-8680-99c90a6dd0b4","assayToken":"65798013-7b6e-4ba7-96a1-d827a02dc509","Spectrometry technique":{"id":26,"name":"LC/MS","parent":{"id":88,"class":"TemplateField"},"class":"org.dbnp.gdt.TemplateFieldListItem"},"name":"Lipidomics profile"}]'''
									break
							}
							break
						case 'gscf/rest/getSamples':

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
								case '32012ce2-ec1e-4e21-9852-a7f527757aaf':
									strResponse = new File('static/assay4_samples').text
									break
								case 'bc117e31-f66e-474a-9b55-26ca94add48b':
									strResponse = new File('static/assay5_samples').text
									break
								case 'f80878f9-791a-4045-af01-c160bfdc7795':
									strResponse = new File('static/assay6_samples').text
									break
								case '65798013-7b6e-4ba7-96a1-d827a02dc509':
									strResponse = new File('static/assay7_samples').text
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
