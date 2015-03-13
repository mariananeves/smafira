//import com.testapp.Role
//import com.testapp.Account
//import com.testapp.AccountRole

class BootStrap {

    def init = { servletContext ->

//        def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
//        def userRole = new Role(authority: 'ROLE_USER').save(flush: true)
//
//        def testUser = new Account(username: 'me', password: 'password')
//        testUser.save(flush: true)
//
//        AccountRole.create testUser, adminRole, true
//
//        assert Account.count() == 1
//        assert Role.count() == 2
//        assert AccountRole.count() == 1
    }
    def destroy = {
    }
}
