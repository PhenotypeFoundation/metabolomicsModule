class ModuleFilters {

	// define filters
	def filters = {
				
		adminPages(controller: '*', action: '*' ) {
			before = { 
				
				//block Feature Property admin (temp)
				if (params.controller == 'featureProperty' && params.action == 'list' && !session.user.isAdministrator){
					redirect(controller: 'home' )
				}
			}
		}
	}
}

