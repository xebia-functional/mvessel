package com.fortysevendeg.android.sqlite.metadata

import java.sql.{ResultSet, Connection}

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait SQLDroidDatabaseMetaDataSupportSpecification
  extends Specification
  with Mockito {

  trait WithMockedConnection
    extends Scope {

    val connection = mock[Connection]

    val sqlDroid = new SQLDroidDatabaseMetaData(connection)

  }

}

class SQLDroidDatabaseMetaDataSupportSpec
  extends SQLDroidDatabaseMetaDataSupportSpecification {

  "allProceduresAreCallable" should {

    "return false" in new WithMockedConnection {
      sqlDroid.allProceduresAreCallable must beFalse
    }
  }

  "allTablesAreSelectable" should {

    "return true" in new WithMockedConnection {
      sqlDroid.allTablesAreSelectable must beTrue
    }
  }

  "autoCommitFailureClosesAllResultSets" should {

    "return false" in new WithMockedConnection {
      sqlDroid.autoCommitFailureClosesAllResultSets must beFalse
    }
  }

  "dataDefinitionCausesTransactionCommit" should {

    "return false" in new WithMockedConnection {
      sqlDroid.dataDefinitionCausesTransactionCommit must beFalse
    }
  }

  "dataDefinitionIgnoredInTransactions" should {

    "return false" in new WithMockedConnection {
      sqlDroid.dataDefinitionIgnoredInTransactions must beFalse
    }
  }

  "deletesAreDetected" should {

    "return false" in new WithMockedConnection {
      sqlDroid.deletesAreDetected(0) must beFalse
    }
  }

  "doesMaxRowSizeIncludeBlobs" should {

    "return false" in new WithMockedConnection {
      sqlDroid.doesMaxRowSizeIncludeBlobs must beFalse
    }
  }

  "generatedKeyAlwaysReturned" should {

    "return false" in new WithMockedConnection {
      sqlDroid.generatedKeyAlwaysReturned must beFalse
    }
  }

  "insertsAreDetected" should {

    "return false" in new WithMockedConnection {
      sqlDroid.insertsAreDetected(0) must beFalse
    }
  }

  "isCatalogAtStart" should {

    "return true" in new WithMockedConnection {
      sqlDroid.isCatalogAtStart must beTrue
    }
  }

  "locatorsUpdateCopy" should {

    "return false" in new WithMockedConnection {
      sqlDroid.locatorsUpdateCopy must beFalse
    }
  }

  "nullPlusNonNullIsNull" should {

    "return true" in new WithMockedConnection {
      sqlDroid.nullPlusNonNullIsNull must beTrue
    }
  }

  "nullsAreSortedAtEnd" should {

    "return false" in new WithMockedConnection {
      sqlDroid.nullsAreSortedAtEnd must beFalse
    }
  }

  "nullsAreSortedAtStart" should {

    "return true" in new WithMockedConnection {
      sqlDroid.nullsAreSortedAtStart must beTrue
    }
  }

  "nullsAreSortedHigh" should {

    "return true" in new WithMockedConnection {
      sqlDroid.nullsAreSortedHigh must beTrue
    }
  }

  "nullsAreSortedLow" should {

    "return false" in new WithMockedConnection {
      sqlDroid.nullsAreSortedLow must beFalse
    }
  }

  "othersDeletesAreVisible" should {

    "return false" in new WithMockedConnection {
      sqlDroid.othersDeletesAreVisible(0) must beFalse
    }
  }

  "othersInsertsAreVisible" should {

    "return false" in new WithMockedConnection {
      sqlDroid.othersInsertsAreVisible(0) must beFalse
    }
  }

  "othersUpdatesAreVisible" should {

    "return false" in new WithMockedConnection {
      sqlDroid.othersUpdatesAreVisible(0) must beFalse
    }
  }

  "ownDeletesAreVisible" should {

    "return false" in new WithMockedConnection {
      sqlDroid.ownDeletesAreVisible(0) must beFalse
    }
  }

  "ownInsertsAreVisible" should {

    "return false" in new WithMockedConnection {
      sqlDroid.ownInsertsAreVisible(0) must beFalse
    }
  }

  "ownUpdatesAreVisible" should {

    "return false" in new WithMockedConnection {
      sqlDroid.ownUpdatesAreVisible(0) must beFalse
    }
  }

  "storesLowerCaseIdentifiers" should {

    "return false" in new WithMockedConnection {
      sqlDroid.storesLowerCaseIdentifiers must beFalse
    }
  }

  "storesLowerCaseQuotedIdentifiers" should {

    "return false" in new WithMockedConnection {
      sqlDroid.storesLowerCaseQuotedIdentifiers must beFalse
    }
  }

  "storesMixedCaseIdentifiers" should {

    "return true" in new WithMockedConnection {
      sqlDroid.storesMixedCaseIdentifiers must beTrue
    }
  }

  "storesMixedCaseQuotedIdentifiers" should {

    "return false" in new WithMockedConnection {
      sqlDroid.storesMixedCaseQuotedIdentifiers must beFalse
    }
  }

  "storesUpperCaseIdentifiers" should {

    "return false" in new WithMockedConnection {
      sqlDroid.storesUpperCaseIdentifiers must beFalse
    }
  }

  "storesUpperCaseQuotedIdentifiers" should {

    "return false" in new WithMockedConnection {
      sqlDroid.storesUpperCaseQuotedIdentifiers must beFalse
    }
  }

  "supportsAlterTableWithAddColumn" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsAlterTableWithAddColumn must beFalse
    }
  }

  "supportsAlterTableWithDropColumn" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsAlterTableWithDropColumn must beFalse
    }
  }

  "supportsANSI92EntryLevelSQL" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsANSI92EntryLevelSQL must beFalse
    }
  }

  "supportsANSI92FullSQL" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsANSI92FullSQL must beFalse
    }
  }

  "supportsANSI92IntermediateSQL" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsANSI92IntermediateSQL must beFalse
    }
  }

  "supportsBatchUpdates" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsBatchUpdates must beTrue
    }
  }

  "supportsCatalogsInDataManipulation" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsCatalogsInDataManipulation must beFalse
    }
  }

  "supportsCatalogsInIndexDefinitions" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsCatalogsInIndexDefinitions must beFalse
    }
  }

  "supportsCatalogsInPrivilegeDefinitions" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsCatalogsInPrivilegeDefinitions must beFalse
    }
  }

  "supportsCatalogsInProcedureCalls" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsCatalogsInProcedureCalls must beFalse
    }
  }

  "supportsCatalogsInTableDefinitions" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsCatalogsInTableDefinitions must beFalse
    }
  }

  "supportsColumnAliasing" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsColumnAliasing must beTrue
    }
  }

  "supportsConvert" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsConvert must beFalse
    }
  }

  "supportsConvert" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsConvert must beFalse
    }
  }

  "supportsCoreSQLGrammar" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsCoreSQLGrammar must beTrue
    }
  }

  "supportsCorrelatedSubqueries" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsCorrelatedSubqueries must beFalse
    }
  }

  "supportsDataDefinitionAndDataManipulationTransactions" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsDataDefinitionAndDataManipulationTransactions must beTrue
    }
  }

  "supportsDataManipulationTransactionsOnly" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsDataManipulationTransactionsOnly must beFalse
    }
  }

  "supportsDifferentTableCorrelationNames" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsDifferentTableCorrelationNames must beFalse
    }
  }

  "supportsExpressionsInOrderBy" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsExpressionsInOrderBy must beTrue
    }
  }

  "supportsExtendedSQLGrammar" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsExtendedSQLGrammar must beFalse
    }
  }

  "supportsFullOuterJoins" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsFullOuterJoins must beFalse
    }
  }

  "supportsGetGeneratedKeys" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsGetGeneratedKeys must beTrue
    }
  }

  "supportsGroupBy" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsGroupBy must beTrue
    }
  }

  "supportsGroupByBeyondSelect" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsGroupByBeyondSelect must beFalse
    }
  }

  "supportsGroupByUnrelated" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsGroupByUnrelated must beFalse
    }
  }

  "supportsIntegrityEnhancementFacility" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsIntegrityEnhancementFacility must beFalse
    }
  }

  "supportsLikeEscapeClause" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsLikeEscapeClause must beFalse
    }
  }

  "supportsLimitedOuterJoins" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsLimitedOuterJoins must beTrue
    }
  }

  "supportsMinimumSQLGrammar" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsMinimumSQLGrammar must beTrue
    }
  }

  "supportsMixedCaseIdentifiers" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsMixedCaseIdentifiers must beTrue
    }
  }

  "supportsMixedCaseQuotedIdentifiers" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsMixedCaseQuotedIdentifiers must beFalse
    }
  }

  "supportsMultipleOpenResults" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsMultipleOpenResults must beFalse
    }
  }

  "supportsMultipleResultSets" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsMultipleResultSets must beFalse
    }
  }

  "supportsMultipleTransactions" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsMultipleTransactions must beTrue
    }
  }

  "supportsNamedParameters" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsNamedParameters must beTrue
    }
  }

  "supportsNonNullableColumns" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsNonNullableColumns must beTrue
    }
  }

  "supportsOpenCursorsAcrossCommit" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsOpenCursorsAcrossCommit must beFalse
    }
  }

  "supportsOpenCursorsAcrossRollback" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsOpenCursorsAcrossRollback must beFalse
    }
  }

  "supportsOpenStatementsAcrossCommit" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsOpenStatementsAcrossCommit must beFalse
    }
  }

  "supportsOpenStatementsAcrossRollback" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsOpenStatementsAcrossRollback must beFalse
    }
  }

  "supportsOrderByUnrelated" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsOrderByUnrelated must beFalse
    }
  }

  "supportsOuterJoins" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsOuterJoins must beTrue
    }
  }

  "supportsPositionedDelete" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsPositionedDelete must beFalse
    }
  }

  "supportsPositionedUpdate" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsPositionedUpdate must beFalse
    }
  }

  "supportsResultSetConcurrency" should {

    "return false when intType is TYPE_SCROLL_SENSITIVE" in
      new WithMockedConnection {
      sqlDroid.supportsResultSetConcurrency(
        ResultSet.TYPE_SCROLL_SENSITIVE,
        ResultSet.CONCUR_READ_ONLY) must beFalse
    }

    "return false when intType is TYPE_SCROLL_INSENSITIVE" in
      new WithMockedConnection {
      sqlDroid.supportsResultSetConcurrency(
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY) must beFalse
    }

    "return false when concurrency is CONCUR_UPDATABLE" in
      new WithMockedConnection {
      sqlDroid.supportsResultSetConcurrency(
        ResultSet.TYPE_FORWARD_ONLY,
        ResultSet.CONCUR_UPDATABLE) must beFalse
    }

    "return true when type is TYPE_FORWARD_ONLY and concurrency is CONCUR_READ_ONLY" in
      new WithMockedConnection {
      sqlDroid.supportsResultSetConcurrency(
        ResultSet.TYPE_FORWARD_ONLY,
        ResultSet.CONCUR_READ_ONLY) must beTrue
    }
  }

  "supportsResultSetHoldability" should {

    "return true when holdability is CLOSE_CURSORS_AT_COMMIT" in new WithMockedConnection {
      sqlDroid.supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT) must beTrue
    }

    "return false when holdability is HOLD_CURSORS_OVER_COMMIT" in new WithMockedConnection {
      sqlDroid.supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT) must beFalse
    }
  }

  "supportsResultSetType" should {

    "return false when intType is TYPE_SCROLL_SENSITIVE" in
      new WithMockedConnection {
        sqlDroid.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE) must beFalse
      }

    "return false when intType is TYPE_SCROLL_INSENSITIVE" in
      new WithMockedConnection {
        sqlDroid.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE) must beFalse
      }

    "return true when type is TYPE_FORWARD_ONLY" in
      new WithMockedConnection {
        sqlDroid.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY) must beTrue
      }
  }

  "supportsSavepoints" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsSavepoints must beFalse
    }
  }

  "supportsSchemasInDataManipulation" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsSchemasInDataManipulation must beFalse
    }
  }

  "supportsSchemasInIndexDefinitions" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsSchemasInIndexDefinitions must beFalse
    }
  }

  "supportsSchemasInPrivilegeDefinitions" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsSchemasInPrivilegeDefinitions must beFalse
    }
  }

  "supportsSchemasInProcedureCalls" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsSchemasInProcedureCalls must beFalse
    }
  }

  "supportsSchemasInTableDefinitions" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsSchemasInTableDefinitions must beFalse
    }
  }

  "supportsSelectForUpdate" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsSelectForUpdate must beFalse
    }
  }

  "supportsStatementPooling" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsStatementPooling must beFalse
    }
  }

  "supportsStoredFunctionsUsingCallSyntax" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsStoredFunctionsUsingCallSyntax must beFalse
    }
  }

  "supportsStoredProcedures" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsStoredProcedures must beFalse
    }
  }

  "supportsSubqueriesInComparisons" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsSubqueriesInComparisons must beFalse
    }
  }

  "supportsSubqueriesInExists" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsSubqueriesInExists must beTrue
    }
  }

  "supportsSubqueriesInIns" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsSubqueriesInIns must beTrue
    }
  }

  "supportsSubqueriesInQuantifieds" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsSubqueriesInQuantifieds must beFalse
    }
  }

  "supportsTableCorrelationNames" should {

    "return false" in new WithMockedConnection {
      sqlDroid.supportsTableCorrelationNames must beFalse
    }
  }

  "supportsTransactionIsolationLevel" should {

    "return false when the transaction is TRANSACTION_NONE" in new WithMockedConnection {
      sqlDroid.supportsTransactionIsolationLevel(
        Connection.TRANSACTION_NONE) must beFalse
    }

    "return false when the transaction is TRANSACTION_READ_COMMITTED" in new WithMockedConnection {
      sqlDroid.supportsTransactionIsolationLevel(
        Connection.TRANSACTION_READ_COMMITTED) must beFalse
    }

    "return false when the transaction is TRANSACTION_READ_UNCOMMITTED" in new WithMockedConnection {
      sqlDroid.supportsTransactionIsolationLevel(
        Connection.TRANSACTION_READ_UNCOMMITTED) must beFalse
    }

    "return false when the transaction is TRANSACTION_REPEATABLE_READ" in new WithMockedConnection {
      sqlDroid.supportsTransactionIsolationLevel(
        Connection.TRANSACTION_REPEATABLE_READ) must beFalse
    }

    "return false when the transaction is TRANSACTION_SERIALIZABLE" in new WithMockedConnection {
      sqlDroid.supportsTransactionIsolationLevel(
        Connection.TRANSACTION_SERIALIZABLE) must beTrue
    }
  }

  "supportsTransactions" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsTransactions must beTrue
    }
  }

  "supportsUnion" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsUnion must beTrue
    }
  }

  "supportsUnionAll" should {

    "return true" in new WithMockedConnection {
      sqlDroid.supportsUnionAll must beTrue
    }
  }

  "updatesAreDetected" should {

    "return false" in new WithMockedConnection {
      sqlDroid.updatesAreDetected(0) must beFalse
    }
  }

  "usesLocalFilePerTable" should {

    "return false" in new WithMockedConnection {
      sqlDroid.usesLocalFilePerTable must beFalse
    }
  }

  "usesLocalFiles" should {

    "return true" in new WithMockedConnection {
      sqlDroid.usesLocalFiles must beTrue
    }
  }

}
