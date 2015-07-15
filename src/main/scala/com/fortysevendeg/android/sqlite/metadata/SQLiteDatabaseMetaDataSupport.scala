package com.fortysevendeg.android.sqlite.metadata

import java.sql.{Connection, ResultSet, DatabaseMetaData}

trait SQLiteDatabaseMetaDataSupport
  extends DatabaseMetaData {

  override def allProceduresAreCallable(): Boolean = false

  override def allTablesAreSelectable(): Boolean = true

  override def autoCommitFailureClosesAllResultSets(): Boolean = false

  override def dataDefinitionCausesTransactionCommit(): Boolean = false

  override def dataDefinitionIgnoredInTransactions(): Boolean = false

  override def deletesAreDetected(`type`: Int): Boolean = false

  override def doesMaxRowSizeIncludeBlobs(): Boolean = false

  override def generatedKeyAlwaysReturned(): Boolean = false

  override def insertsAreDetected(`type`: Int): Boolean = false

  override def isCatalogAtStart: Boolean = true

  override def locatorsUpdateCopy(): Boolean = false

  override def nullPlusNonNullIsNull(): Boolean = true

  override def nullsAreSortedAtEnd: Boolean = !nullsAreSortedAtStart

  override def nullsAreSortedAtStart: Boolean = true

  override def nullsAreSortedHigh: Boolean = true

  override def nullsAreSortedLow: Boolean = !nullsAreSortedHigh

  override def othersDeletesAreVisible(intType: Int): Boolean = false

  override def othersInsertsAreVisible(intType: Int): Boolean = false

  override def othersUpdatesAreVisible(intType: Int): Boolean = false

  override def ownDeletesAreVisible(intType: Int): Boolean = false

  override def ownInsertsAreVisible(intType: Int): Boolean = false

  override def ownUpdatesAreVisible(intType: Int): Boolean = false

  override def storesLowerCaseIdentifiers(): Boolean = false

  override def storesLowerCaseQuotedIdentifiers(): Boolean = false

  override def storesMixedCaseIdentifiers(): Boolean = true

  override def storesMixedCaseQuotedIdentifiers(): Boolean = false

  override def storesUpperCaseIdentifiers(): Boolean = false

  override def storesUpperCaseQuotedIdentifiers(): Boolean = false

  override def supportsAlterTableWithAddColumn(): Boolean = false

  override def supportsAlterTableWithDropColumn(): Boolean = false

  override def supportsANSI92EntryLevelSQL(): Boolean = false

  override def supportsANSI92FullSQL(): Boolean = false

  override def supportsANSI92IntermediateSQL(): Boolean = false

  override def supportsBatchUpdates(): Boolean = true

  override def supportsCatalogsInDataManipulation(): Boolean = false

  override def supportsCatalogsInIndexDefinitions(): Boolean = false

  override def supportsCatalogsInPrivilegeDefinitions(): Boolean = false

  override def supportsCatalogsInProcedureCalls(): Boolean = false

  override def supportsCatalogsInTableDefinitions(): Boolean = false

  override def supportsColumnAliasing(): Boolean = true

  override def supportsConvert(): Boolean = false

  override def supportsConvert(fromType: Int, toType: Int): Boolean = false

  override def supportsCoreSQLGrammar(): Boolean = true

  override def supportsCorrelatedSubqueries(): Boolean = false

  override def supportsDataDefinitionAndDataManipulationTransactions(): Boolean = true

  override def supportsDataManipulationTransactionsOnly(): Boolean = false

  override def supportsDifferentTableCorrelationNames(): Boolean = false

  override def supportsExpressionsInOrderBy(): Boolean = true

  override def supportsExtendedSQLGrammar(): Boolean = false

  override def supportsFullOuterJoins(): Boolean = false

  override def supportsGetGeneratedKeys(): Boolean = true

  override def supportsGroupBy(): Boolean = true

  override def supportsGroupByBeyondSelect(): Boolean = false

  override def supportsGroupByUnrelated(): Boolean = false

  override def supportsIntegrityEnhancementFacility(): Boolean = false

  override def supportsLikeEscapeClause(): Boolean = false

  override def supportsLimitedOuterJoins(): Boolean = true

  override def supportsMinimumSQLGrammar(): Boolean = true

  override def supportsMixedCaseIdentifiers(): Boolean = true

  override def supportsMixedCaseQuotedIdentifiers(): Boolean = false

  override def supportsMultipleOpenResults(): Boolean = false

  override def supportsMultipleResultSets(): Boolean = false

  override def supportsMultipleTransactions(): Boolean = true

  override def supportsNamedParameters(): Boolean = true

  override def supportsNonNullableColumns(): Boolean = true

  override def supportsOpenCursorsAcrossCommit(): Boolean = false

  override def supportsOpenCursorsAcrossRollback(): Boolean = false

  override def supportsOpenStatementsAcrossCommit(): Boolean = false

  override def supportsOpenStatementsAcrossRollback(): Boolean = false

  override def supportsOrderByUnrelated(): Boolean = false

  override def supportsOuterJoins(): Boolean = true

  override def supportsPositionedDelete(): Boolean = false

  override def supportsPositionedUpdate(): Boolean = false

  override def supportsResultSetConcurrency(intType: Int, concurrency: Int): Boolean =
    intType == ResultSet.TYPE_FORWARD_ONLY && concurrency == ResultSet.CONCUR_READ_ONLY

  override def supportsResultSetHoldability(holdability: Int): Boolean =
    holdability == ResultSet.CLOSE_CURSORS_AT_COMMIT

  override def supportsResultSetType(intType: Int): Boolean =
    intType == ResultSet.TYPE_FORWARD_ONLY

  override def supportsSavepoints(): Boolean = false

  override def supportsSchemasInDataManipulation(): Boolean = false

  override def supportsSchemasInIndexDefinitions(): Boolean = false

  override def supportsSchemasInPrivilegeDefinitions(): Boolean = false

  override def supportsSchemasInProcedureCalls(): Boolean = false

  override def supportsSchemasInTableDefinitions(): Boolean = false

  override def supportsSelectForUpdate(): Boolean = false

  override def supportsStatementPooling(): Boolean = false

  override def supportsStoredFunctionsUsingCallSyntax(): Boolean = false

  override def supportsStoredProcedures(): Boolean = false

  override def supportsSubqueriesInComparisons(): Boolean = false

  // TODO - Check
  override def supportsSubqueriesInExists(): Boolean = true

  // TODO - Check
  override def supportsSubqueriesInIns(): Boolean = true

  override def supportsSubqueriesInQuantifieds(): Boolean = false

  override def supportsTableCorrelationNames(): Boolean = false

  override def supportsTransactionIsolationLevel(level: Int): Boolean =
    level == Connection.TRANSACTION_SERIALIZABLE

  override def supportsTransactions(): Boolean = true

  override def supportsUnion(): Boolean = true

  override def supportsUnionAll(): Boolean = true

  override def updatesAreDetected(intType: Int): Boolean = false

  override def usesLocalFilePerTable(): Boolean = false

  override def usesLocalFiles(): Boolean = true

}
