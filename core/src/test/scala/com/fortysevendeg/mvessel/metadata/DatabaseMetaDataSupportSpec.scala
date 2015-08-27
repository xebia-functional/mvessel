package com.fortysevendeg.mvessel.metadata

import java.sql.{ResultSet, Connection}

import com.fortysevendeg.mvessel.TestLogWrapper
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait DatabaseMetaDataSupportSpecification
  extends Specification
  with Mockito {

  trait WithMockedConnection
    extends Scope {

    val connection = mock[Connection]

    val databaseMetaData = new DatabaseMetaData(connection, new TestLogWrapper)

  }

}

class DatabaseMetaDataSupportSpec
  extends DatabaseMetaDataSupportSpecification {

  "allProceduresAreCallable" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.allProceduresAreCallable must beFalse
    }
  }

  "allTablesAreSelectable" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.allTablesAreSelectable must beTrue
    }
  }

  "autoCommitFailureClosesAllResultSets" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.autoCommitFailureClosesAllResultSets must beFalse
    }
  }

  "dataDefinitionCausesTransactionCommit" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.dataDefinitionCausesTransactionCommit must beFalse
    }
  }

  "dataDefinitionIgnoredInTransactions" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.dataDefinitionIgnoredInTransactions must beFalse
    }
  }

  "deletesAreDetected" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.deletesAreDetected(0) must beFalse
    }
  }

  "doesMaxRowSizeIncludeBlobs" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.doesMaxRowSizeIncludeBlobs must beFalse
    }
  }

  "generatedKeyAlwaysReturned" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.generatedKeyAlwaysReturned must beFalse
    }
  }

  "insertsAreDetected" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.insertsAreDetected(0) must beFalse
    }
  }

  "isCatalogAtStart" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.isCatalogAtStart must beTrue
    }
  }

  "locatorsUpdateCopy" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.locatorsUpdateCopy must beFalse
    }
  }

  "nullPlusNonNullIsNull" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.nullPlusNonNullIsNull must beTrue
    }
  }

  "nullsAreSortedAtEnd" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.nullsAreSortedAtEnd must beFalse
    }
  }

  "nullsAreSortedAtStart" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.nullsAreSortedAtStart must beTrue
    }
  }

  "nullsAreSortedHigh" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.nullsAreSortedHigh must beTrue
    }
  }

  "nullsAreSortedLow" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.nullsAreSortedLow must beFalse
    }
  }

  "othersDeletesAreVisible" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.othersDeletesAreVisible(0) must beFalse
    }
  }

  "othersInsertsAreVisible" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.othersInsertsAreVisible(0) must beFalse
    }
  }

  "othersUpdatesAreVisible" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.othersUpdatesAreVisible(0) must beFalse
    }
  }

  "ownDeletesAreVisible" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.ownDeletesAreVisible(0) must beFalse
    }
  }

  "ownInsertsAreVisible" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.ownInsertsAreVisible(0) must beFalse
    }
  }

  "ownUpdatesAreVisible" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.ownUpdatesAreVisible(0) must beFalse
    }
  }

  "storesLowerCaseIdentifiers" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.storesLowerCaseIdentifiers must beFalse
    }
  }

  "storesLowerCaseQuotedIdentifiers" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.storesLowerCaseQuotedIdentifiers must beFalse
    }
  }

  "storesMixedCaseIdentifiers" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.storesMixedCaseIdentifiers must beTrue
    }
  }

  "storesMixedCaseQuotedIdentifiers" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.storesMixedCaseQuotedIdentifiers must beFalse
    }
  }

  "storesUpperCaseIdentifiers" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.storesUpperCaseIdentifiers must beFalse
    }
  }

  "storesUpperCaseQuotedIdentifiers" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.storesUpperCaseQuotedIdentifiers must beFalse
    }
  }

  "supportsAlterTableWithAddColumn" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsAlterTableWithAddColumn must beFalse
    }
  }

  "supportsAlterTableWithDropColumn" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsAlterTableWithDropColumn must beFalse
    }
  }

  "supportsANSI92EntryLevelSQL" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsANSI92EntryLevelSQL must beFalse
    }
  }

  "supportsANSI92FullSQL" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsANSI92FullSQL must beFalse
    }
  }

  "supportsANSI92IntermediateSQL" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsANSI92IntermediateSQL must beFalse
    }
  }

  "supportsBatchUpdates" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsBatchUpdates must beTrue
    }
  }

  "supportsCatalogsInDataManipulation" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsCatalogsInDataManipulation must beFalse
    }
  }

  "supportsCatalogsInIndexDefinitions" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsCatalogsInIndexDefinitions must beFalse
    }
  }

  "supportsCatalogsInPrivilegeDefinitions" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsCatalogsInPrivilegeDefinitions must beFalse
    }
  }

  "supportsCatalogsInProcedureCalls" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsCatalogsInProcedureCalls must beFalse
    }
  }

  "supportsCatalogsInTableDefinitions" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsCatalogsInTableDefinitions must beFalse
    }
  }

  "supportsColumnAliasing" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsColumnAliasing must beTrue
    }
  }

  "supportsConvert" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsConvert must beFalse
    }
  }

  "supportsConvert" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsConvert must beFalse
    }
  }

  "supportsCoreSQLGrammar" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsCoreSQLGrammar must beTrue
    }
  }

  "supportsCorrelatedSubqueries" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsCorrelatedSubqueries must beFalse
    }
  }

  "supportsDataDefinitionAndDataManipulationTransactions" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsDataDefinitionAndDataManipulationTransactions must beTrue
    }
  }

  "supportsDataManipulationTransactionsOnly" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsDataManipulationTransactionsOnly must beFalse
    }
  }

  "supportsDifferentTableCorrelationNames" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsDifferentTableCorrelationNames must beFalse
    }
  }

  "supportsExpressionsInOrderBy" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsExpressionsInOrderBy must beTrue
    }
  }

  "supportsExtendedSQLGrammar" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsExtendedSQLGrammar must beFalse
    }
  }

  "supportsFullOuterJoins" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsFullOuterJoins must beFalse
    }
  }

  "supportsGetGeneratedKeys" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsGetGeneratedKeys must beTrue
    }
  }

  "supportsGroupBy" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsGroupBy must beTrue
    }
  }

  "supportsGroupByBeyondSelect" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsGroupByBeyondSelect must beFalse
    }
  }

  "supportsGroupByUnrelated" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsGroupByUnrelated must beFalse
    }
  }

  "supportsIntegrityEnhancementFacility" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsIntegrityEnhancementFacility must beFalse
    }
  }

  "supportsLikeEscapeClause" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsLikeEscapeClause must beFalse
    }
  }

  "supportsLimitedOuterJoins" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsLimitedOuterJoins must beTrue
    }
  }

  "supportsMinimumSQLGrammar" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsMinimumSQLGrammar must beTrue
    }
  }

  "supportsMixedCaseIdentifiers" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsMixedCaseIdentifiers must beTrue
    }
  }

  "supportsMixedCaseQuotedIdentifiers" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsMixedCaseQuotedIdentifiers must beFalse
    }
  }

  "supportsMultipleOpenResults" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsMultipleOpenResults must beFalse
    }
  }

  "supportsMultipleResultSets" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsMultipleResultSets must beFalse
    }
  }

  "supportsMultipleTransactions" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsMultipleTransactions must beTrue
    }
  }

  "supportsNamedParameters" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsNamedParameters must beTrue
    }
  }

  "supportsNonNullableColumns" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsNonNullableColumns must beTrue
    }
  }

  "supportsOpenCursorsAcrossCommit" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsOpenCursorsAcrossCommit must beFalse
    }
  }

  "supportsOpenCursorsAcrossRollback" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsOpenCursorsAcrossRollback must beFalse
    }
  }

  "supportsOpenStatementsAcrossCommit" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsOpenStatementsAcrossCommit must beFalse
    }
  }

  "supportsOpenStatementsAcrossRollback" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsOpenStatementsAcrossRollback must beFalse
    }
  }

  "supportsOrderByUnrelated" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsOrderByUnrelated must beFalse
    }
  }

  "supportsOuterJoins" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsOuterJoins must beTrue
    }
  }

  "supportsPositionedDelete" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsPositionedDelete must beFalse
    }
  }

  "supportsPositionedUpdate" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsPositionedUpdate must beFalse
    }
  }

  "supportsResultSetConcurrency" should {

    "return false when intType is TYPE_SCROLL_SENSITIVE" in
      new WithMockedConnection {
      databaseMetaData.supportsResultSetConcurrency(
        ResultSet.TYPE_SCROLL_SENSITIVE,
        ResultSet.CONCUR_READ_ONLY) must beFalse
    }

    "return false when intType is TYPE_SCROLL_INSENSITIVE" in
      new WithMockedConnection {
      databaseMetaData.supportsResultSetConcurrency(
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY) must beFalse
    }

    "return false when concurrency is CONCUR_UPDATABLE" in
      new WithMockedConnection {
      databaseMetaData.supportsResultSetConcurrency(
        ResultSet.TYPE_FORWARD_ONLY,
        ResultSet.CONCUR_UPDATABLE) must beFalse
    }

    "return true when type is TYPE_FORWARD_ONLY and concurrency is CONCUR_READ_ONLY" in
      new WithMockedConnection {
      databaseMetaData.supportsResultSetConcurrency(
        ResultSet.TYPE_FORWARD_ONLY,
        ResultSet.CONCUR_READ_ONLY) must beTrue
    }
  }

  "supportsResultSetHoldability" should {

    "return true when holdability is CLOSE_CURSORS_AT_COMMIT" in new WithMockedConnection {
      databaseMetaData.supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT) must beTrue
    }

    "return false when holdability is HOLD_CURSORS_OVER_COMMIT" in new WithMockedConnection {
      databaseMetaData.supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT) must beFalse
    }
  }

  "supportsResultSetType" should {

    "return false when intType is TYPE_SCROLL_SENSITIVE" in
      new WithMockedConnection {
        databaseMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE) must beFalse
      }

    "return false when intType is TYPE_SCROLL_INSENSITIVE" in
      new WithMockedConnection {
        databaseMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE) must beFalse
      }

    "return true when type is TYPE_FORWARD_ONLY" in
      new WithMockedConnection {
        databaseMetaData.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY) must beTrue
      }
  }

  "supportsSavepoints" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsSavepoints must beFalse
    }
  }

  "supportsSchemasInDataManipulation" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsSchemasInDataManipulation must beFalse
    }
  }

  "supportsSchemasInIndexDefinitions" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsSchemasInIndexDefinitions must beFalse
    }
  }

  "supportsSchemasInPrivilegeDefinitions" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsSchemasInPrivilegeDefinitions must beFalse
    }
  }

  "supportsSchemasInProcedureCalls" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsSchemasInProcedureCalls must beFalse
    }
  }

  "supportsSchemasInTableDefinitions" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsSchemasInTableDefinitions must beFalse
    }
  }

  "supportsSelectForUpdate" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsSelectForUpdate must beFalse
    }
  }

  "supportsStatementPooling" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsStatementPooling must beFalse
    }
  }

  "supportsStoredFunctionsUsingCallSyntax" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsStoredFunctionsUsingCallSyntax must beFalse
    }
  }

  "supportsStoredProcedures" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsStoredProcedures must beFalse
    }
  }

  "supportsSubqueriesInComparisons" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsSubqueriesInComparisons must beFalse
    }
  }

  "supportsSubqueriesInExists" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsSubqueriesInExists must beTrue
    }
  }

  "supportsSubqueriesInIns" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsSubqueriesInIns must beTrue
    }
  }

  "supportsSubqueriesInQuantifieds" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsSubqueriesInQuantifieds must beFalse
    }
  }

  "supportsTableCorrelationNames" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.supportsTableCorrelationNames must beFalse
    }
  }

  "supportsTransactionIsolationLevel" should {

    "return false when the transaction is TRANSACTION_NONE" in new WithMockedConnection {
      databaseMetaData.supportsTransactionIsolationLevel(
        Connection.TRANSACTION_NONE) must beFalse
    }

    "return false when the transaction is TRANSACTION_READ_COMMITTED" in new WithMockedConnection {
      databaseMetaData.supportsTransactionIsolationLevel(
        Connection.TRANSACTION_READ_COMMITTED) must beFalse
    }

    "return false when the transaction is TRANSACTION_READ_UNCOMMITTED" in new WithMockedConnection {
      databaseMetaData.supportsTransactionIsolationLevel(
        Connection.TRANSACTION_READ_UNCOMMITTED) must beFalse
    }

    "return false when the transaction is TRANSACTION_REPEATABLE_READ" in new WithMockedConnection {
      databaseMetaData.supportsTransactionIsolationLevel(
        Connection.TRANSACTION_REPEATABLE_READ) must beFalse
    }

    "return false when the transaction is TRANSACTION_SERIALIZABLE" in new WithMockedConnection {
      databaseMetaData.supportsTransactionIsolationLevel(
        Connection.TRANSACTION_SERIALIZABLE) must beTrue
    }
  }

  "supportsTransactions" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsTransactions must beTrue
    }
  }

  "supportsUnion" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsUnion must beTrue
    }
  }

  "supportsUnionAll" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.supportsUnionAll must beTrue
    }
  }

  "updatesAreDetected" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.updatesAreDetected(0) must beFalse
    }
  }

  "usesLocalFilePerTable" should {

    "return false" in new WithMockedConnection {
      databaseMetaData.usesLocalFilePerTable must beFalse
    }
  }

  "usesLocalFiles" should {

    "return true" in new WithMockedConnection {
      databaseMetaData.usesLocalFiles must beTrue
    }
  }

}
