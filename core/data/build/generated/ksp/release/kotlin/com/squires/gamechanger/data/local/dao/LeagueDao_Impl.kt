package com.squires.gamechanger.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.squires.gamechanger.`data`.local.entity.LeagueEntity
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
public class LeagueDao_Impl(
  __db: RoomDatabase,
) : LeagueDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfLeagueEntity: EntityInsertAdapter<LeagueEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfLeagueEntity = object : EntityInsertAdapter<LeagueEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `leagues` (`id`,`name`,`sport`,`badgeUrl`,`country`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: LeagueEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.sport)
        val _tmpBadgeUrl: String? = entity.badgeUrl
        if (_tmpBadgeUrl == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpBadgeUrl)
        }
        val _tmpCountry: String? = entity.country
        if (_tmpCountry == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpCountry)
        }
      }
    }
  }

  public override suspend fun insertAll(leagues: List<LeagueEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfLeagueEntity.insert(_connection, leagues)
  }

  public override fun getLeagues(): Flow<List<LeagueEntity>> {
    val _sql: String = "SELECT * FROM leagues ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("leagues")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfSport: Int = getColumnIndexOrThrow(_stmt, "sport")
        val _columnIndexOfBadgeUrl: Int = getColumnIndexOrThrow(_stmt, "badgeUrl")
        val _columnIndexOfCountry: Int = getColumnIndexOrThrow(_stmt, "country")
        val _result: MutableList<LeagueEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: LeagueEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpSport: String
          _tmpSport = _stmt.getText(_columnIndexOfSport)
          val _tmpBadgeUrl: String?
          if (_stmt.isNull(_columnIndexOfBadgeUrl)) {
            _tmpBadgeUrl = null
          } else {
            _tmpBadgeUrl = _stmt.getText(_columnIndexOfBadgeUrl)
          }
          val _tmpCountry: String?
          if (_stmt.isNull(_columnIndexOfCountry)) {
            _tmpCountry = null
          } else {
            _tmpCountry = _stmt.getText(_columnIndexOfCountry)
          }
          _item = LeagueEntity(_tmpId,_tmpName,_tmpSport,_tmpBadgeUrl,_tmpCountry)
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
