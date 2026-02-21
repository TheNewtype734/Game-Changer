package com.squires.gamechanger.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.squires.gamechanger.`data`.local.entity.TeamEntity
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TeamDao_Impl(
  __db: RoomDatabase,
) : TeamDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTeamEntity: EntityInsertAdapter<TeamEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTeamEntity = object : EntityInsertAdapter<TeamEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `teams` (`id`,`name`,`leagueName`,`badgeUrl`,`sport`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TeamEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.leagueName)
        val _tmpBadgeUrl: String? = entity.badgeUrl
        if (_tmpBadgeUrl == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpBadgeUrl)
        }
        val _tmpSport: String? = entity.sport
        if (_tmpSport == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpSport)
        }
      }
    }
  }

  public override suspend fun insertAll(teams: List<TeamEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTeamEntity.insert(_connection, teams)
  }

  public override fun getTeamsForLeague(leagueName: String): Flow<List<TeamEntity>> {
    val _sql: String = "SELECT * FROM teams WHERE leagueName = ? ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("teams")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, leagueName)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfLeagueName: Int = getColumnIndexOrThrow(_stmt, "leagueName")
        val _columnIndexOfBadgeUrl: Int = getColumnIndexOrThrow(_stmt, "badgeUrl")
        val _columnIndexOfSport: Int = getColumnIndexOrThrow(_stmt, "sport")
        val _result: MutableList<TeamEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TeamEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpLeagueName: String
          _tmpLeagueName = _stmt.getText(_columnIndexOfLeagueName)
          val _tmpBadgeUrl: String?
          if (_stmt.isNull(_columnIndexOfBadgeUrl)) {
            _tmpBadgeUrl = null
          } else {
            _tmpBadgeUrl = _stmt.getText(_columnIndexOfBadgeUrl)
          }
          val _tmpSport: String?
          if (_stmt.isNull(_columnIndexOfSport)) {
            _tmpSport = null
          } else {
            _tmpSport = _stmt.getText(_columnIndexOfSport)
          }
          _item = TeamEntity(_tmpId,_tmpName,_tmpLeagueName,_tmpBadgeUrl,_tmpSport)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
