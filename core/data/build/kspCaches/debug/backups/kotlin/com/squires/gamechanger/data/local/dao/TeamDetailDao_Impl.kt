package com.squires.gamechanger.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.squires.gamechanger.`data`.local.entity.TeamDetailEntity
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TeamDetailDao_Impl(
  __db: RoomDatabase,
) : TeamDetailDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTeamDetailEntity: EntityInsertAdapter<TeamDetailEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTeamDetailEntity = object : EntityInsertAdapter<TeamDetailEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `team_details` (`id`,`name`,`leagueName`,`sport`,`country`,`formedYear`,`description`,`stadium`,`stadiumLocation`,`stadiumCapacity`,`badgeUrl`,`bannerUrl`,`website`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TeamDetailEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.leagueName)
        val _tmpSport: String? = entity.sport
        if (_tmpSport == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpSport)
        }
        val _tmpCountry: String? = entity.country
        if (_tmpCountry == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpCountry)
        }
        val _tmpFormedYear: String? = entity.formedYear
        if (_tmpFormedYear == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpFormedYear)
        }
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpDescription)
        }
        val _tmpStadium: String? = entity.stadium
        if (_tmpStadium == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpStadium)
        }
        val _tmpStadiumLocation: String? = entity.stadiumLocation
        if (_tmpStadiumLocation == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpStadiumLocation)
        }
        val _tmpStadiumCapacity: String? = entity.stadiumCapacity
        if (_tmpStadiumCapacity == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpStadiumCapacity)
        }
        val _tmpBadgeUrl: String? = entity.badgeUrl
        if (_tmpBadgeUrl == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpBadgeUrl)
        }
        val _tmpBannerUrl: String? = entity.bannerUrl
        if (_tmpBannerUrl == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmpBannerUrl)
        }
        val _tmpWebsite: String? = entity.website
        if (_tmpWebsite == null) {
          statement.bindNull(13)
        } else {
          statement.bindText(13, _tmpWebsite)
        }
      }
    }
  }

  public override suspend fun insert(teamDetail: TeamDetailEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTeamDetailEntity.insert(_connection, teamDetail)
  }

  public override fun getTeamDetail(teamId: String): Flow<TeamDetailEntity?> {
    val _sql: String = "SELECT * FROM team_details WHERE id = ?"
    return createFlow(__db, false, arrayOf("team_details")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, teamId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfLeagueName: Int = getColumnIndexOrThrow(_stmt, "leagueName")
        val _columnIndexOfSport: Int = getColumnIndexOrThrow(_stmt, "sport")
        val _columnIndexOfCountry: Int = getColumnIndexOrThrow(_stmt, "country")
        val _columnIndexOfFormedYear: Int = getColumnIndexOrThrow(_stmt, "formedYear")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfStadium: Int = getColumnIndexOrThrow(_stmt, "stadium")
        val _columnIndexOfStadiumLocation: Int = getColumnIndexOrThrow(_stmt, "stadiumLocation")
        val _columnIndexOfStadiumCapacity: Int = getColumnIndexOrThrow(_stmt, "stadiumCapacity")
        val _columnIndexOfBadgeUrl: Int = getColumnIndexOrThrow(_stmt, "badgeUrl")
        val _columnIndexOfBannerUrl: Int = getColumnIndexOrThrow(_stmt, "bannerUrl")
        val _columnIndexOfWebsite: Int = getColumnIndexOrThrow(_stmt, "website")
        val _result: TeamDetailEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpLeagueName: String
          _tmpLeagueName = _stmt.getText(_columnIndexOfLeagueName)
          val _tmpSport: String?
          if (_stmt.isNull(_columnIndexOfSport)) {
            _tmpSport = null
          } else {
            _tmpSport = _stmt.getText(_columnIndexOfSport)
          }
          val _tmpCountry: String?
          if (_stmt.isNull(_columnIndexOfCountry)) {
            _tmpCountry = null
          } else {
            _tmpCountry = _stmt.getText(_columnIndexOfCountry)
          }
          val _tmpFormedYear: String?
          if (_stmt.isNull(_columnIndexOfFormedYear)) {
            _tmpFormedYear = null
          } else {
            _tmpFormedYear = _stmt.getText(_columnIndexOfFormedYear)
          }
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpStadium: String?
          if (_stmt.isNull(_columnIndexOfStadium)) {
            _tmpStadium = null
          } else {
            _tmpStadium = _stmt.getText(_columnIndexOfStadium)
          }
          val _tmpStadiumLocation: String?
          if (_stmt.isNull(_columnIndexOfStadiumLocation)) {
            _tmpStadiumLocation = null
          } else {
            _tmpStadiumLocation = _stmt.getText(_columnIndexOfStadiumLocation)
          }
          val _tmpStadiumCapacity: String?
          if (_stmt.isNull(_columnIndexOfStadiumCapacity)) {
            _tmpStadiumCapacity = null
          } else {
            _tmpStadiumCapacity = _stmt.getText(_columnIndexOfStadiumCapacity)
          }
          val _tmpBadgeUrl: String?
          if (_stmt.isNull(_columnIndexOfBadgeUrl)) {
            _tmpBadgeUrl = null
          } else {
            _tmpBadgeUrl = _stmt.getText(_columnIndexOfBadgeUrl)
          }
          val _tmpBannerUrl: String?
          if (_stmt.isNull(_columnIndexOfBannerUrl)) {
            _tmpBannerUrl = null
          } else {
            _tmpBannerUrl = _stmt.getText(_columnIndexOfBannerUrl)
          }
          val _tmpWebsite: String?
          if (_stmt.isNull(_columnIndexOfWebsite)) {
            _tmpWebsite = null
          } else {
            _tmpWebsite = _stmt.getText(_columnIndexOfWebsite)
          }
          _result = TeamDetailEntity(_tmpId,_tmpName,_tmpLeagueName,_tmpSport,_tmpCountry,_tmpFormedYear,_tmpDescription,_tmpStadium,_tmpStadiumLocation,_tmpStadiumCapacity,_tmpBadgeUrl,_tmpBannerUrl,_tmpWebsite)
        } else {
          _result = null
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
