package net.nosegrind.apiframework

class Person implements Serializable {

	private static final long serialVersionUID = 1

	static transients = ['hasBeforeInsert','hasBeforeValidate','hasBeforeUpdate','springSecurityService']

	transient hasBeforeInsert = false
	transient hasBeforeValidate = false
	transient hasBeforeUpdate = false
	transient springSecurityService

	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	Person(String username, String password) {
		this()
		this.username = username
		this.password = password
	}

	@Override
	int hashCode() {
		username?.hashCode() ?: 0
	}

	@Override
	boolean equals(other) {
		is(other) || (other instanceof Person && other.username == username)
	}

	@Override
	String toString() {
		username
	}

	Set<Role> getAuthorities() {
		PersonRole.findAllByPerson(this)*.role
	}

	def beforeInsert() {
		if (!hasBeforeInsert) {
			hasBeforeInsert = true
			encodePassword()
		}
	}

	def afterInsert() {
		hasBeforeInsert = false
	}

	def beforeUpdate() {
		if (!hasBeforeUpdate) {
			if (isDirty('password')) {
				hasBeforeUpdate = true
				encodePassword()
			}
		}
	}

	def afterUpdate() {
		hasBeforeUpdate = false
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
	/*
	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
	*/

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		datasource 'user'
		password column: '`password`'
	}
}
