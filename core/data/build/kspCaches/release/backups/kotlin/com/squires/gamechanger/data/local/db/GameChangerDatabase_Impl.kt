package com.squires.gamechanger.`data`.local.db

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.squires.gamechanger.`data`.local.dao.LeagueDao
import com.squires.gamechanger.`data`.local.dao.LeagueDao_Impl
import com.squires.gamechanger.`data`.local.dao.TeamDao
import com.squires.gamechanger.`data`.local.dao.TeamDao_Impl
import com.squires.gamechanger.`data`.local.dao.TeamDetailDao
import com.squires.gamechanger.`data`.local.dao.TeamDetailDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class GameChangerDatabase_Impl : GameChangerDatabase() {
  private val _leagueDao: Lazy<LeagueDao> = lazy {
    LeagueDao_Impl(this)
  }

  private val _teamDao: Lazy<TeamDao> = lazy {
    TeamDao_Impl(this)
  }

  private val _teamDetailDao: Lazy<TeamDetailDao> = lazy {
    TeamDetailDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "0da9f0333f49fcc0dd97dd5511142d32", "3737892f7e1033e406309bebe883dea3") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `leagues` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `sport` TEXT NOT NULL, `badgeUrl` TEXT, `country` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `teams` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `leagueName` TEXT NOT NULL, `badgeUrl` TEXT, `sport` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `team_details` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `leagueName` TEXT NOT NULL, `sport` TEXT, `country` TEXT, `formedYear` TEXT, `description` TEXT, `stadium` TEXT, `stadiumLocation` TEXT, `stadiumCapacity` TEXT, `badgeUrl` TEXT, `bannerUrl` TEXT, `website` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0da9f0333f49fcc0dd97dd5511142d32')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `leagues`")
        connection.execSQL("DROP TABLE IF EXISTS `teams`")
        connection.execSQL("DROP TABLE IF EXISTS `team_details`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsLeagues: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsLeagues.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsLeagues.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsLeagues.put("sport", TableInfo.Column("sport", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsLeagues.put("badgeUrl", TableInfo.Column("badgeUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsLeagues.put("country", TableInfo.Column("country", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysLeagues: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesLeagues: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoLeagues: TableInfo = TableInfo("leagues", _columnsLeagues, _foreignKeysLeagues, _indicesLeagues)
        val _existingLeagues: TableInfo = read(connection, "leagues")
        if (!_infoLeagues.equals(_existingLeagues)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |leagues(com.squires.gamechanger.data.local.entity.LeagueEntity).
              | Expected:
              |""".trimMargin() + _infoLeagues + """
              |
              | Found:
              |""".trimMargin() + _existingLeagues)
        }
        val _columnsTeams: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTeams.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeams.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeams.put("leagueName", TableInfo.Column("leagueName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeams.put("badgeUrl", TableInfo.Column("badgeUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeams.put("sport", TableInfo.Column("sport", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTeams: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTeams: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTeams: TableInfo = TableInfo("teams", _columnsTeams, _foreignKeysTeams, _indicesTeams)
        val _existingTeams: TableInfo = read(connection, "teams")
        if (!_infoTeams.equals(_existingTeams)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |teams(com.squires.gamechanger.data.local.entity.TeamEntity).
              | Expected:
              |""".trimMargin() + _infoTeams + """
              |
              | Found:
              |""".trimMargin() + _existingTeams)
        }
        val _columnsTeamDetails: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTeamDetails.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeamDetails.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeamDetails.put("leagueName", TableInfo.Column("leagueName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeamDetails.put("sport", TableInfo.Column("sport", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeamDetails.put("country", TableInfo.Column("country", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeamDetails.put("formedYear", TableInfo.Column("formedYear", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeamDetails.put("description", TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeamDetails.put("stadium", TableInfo.Column("stadium", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeamDetails.put("stadiumLocation", TableInfo.Column("stadiumLocation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeamDetails.put("stadiumCapacity", TableInfo.Column("stadiumCapacity", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeamDetails.put("badgeUrl", TableInfo.Column("badgeUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeamDetails.put("bannerUrl", TableInfo.Column("bannerUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTeamDetails.put("website", TableInfo.Column("website", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTeamDetails: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTeamDetails: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTeamDetails: TableInfo = TableInfo("team_details", _columnsTeamDetails, _foreignKeysTeamDetails, _indicesTeamDetails)
        val _existingTeamDetails: TableInfo = read(connection, "team_details")
        if (!_infoTeamDetails.equals(_existingTeamDetails)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |team_details(com.squires.gamechanger.data.local.entity.TeamDetailEntity).
              | Expected:
              |""".trimMargin() + _infoTeamDetails + """
              |
              | Found:
              |""".trimMargin() + _existingTeamDetails)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "leagues", "teams", "team_details")
  }

  public override fun clearAllTables() {
    super.performClear(false, "leagues", "teams", "team_details")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(LeagueDao::class, LeagueDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(TeamDao::class, TeamDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(TeamDetailDao::class, TeamDetailDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun leagueDao(): LeagueDao = _leagueDao.value

  public override fun teamDao(): TeamDao = _teamDao.value

  public override fun teamDetailDao(): TeamDetailDao = _teamDetailDao.value
}
