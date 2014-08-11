package com.infinityappsolutions.server.lib.exceptions;

import java.sql.SQLException;

public class DBException extends IASException {
	private static final long serialVersionUID = -6554118510590118376L;
	private SQLException sqlException = null;

	public DBException(SQLException e) {
		super(
				"A database exception has occurred. Please see the log in the console for stacktrace");
		this.sqlException = e;
	}

	public DBException(Exception e) {
		super(e.getMessage());
	}

	/**
	 * @return The SQL Exception that was responsible for this error.
	 */
	public SQLException getSQLException() {
		return sqlException;
	}

	@Override
	public String getExtendedMessage() {
		if (sqlException != null)
			return sqlException.getMessage();
		else
			return super.getExtendedMessage();
	}
}
