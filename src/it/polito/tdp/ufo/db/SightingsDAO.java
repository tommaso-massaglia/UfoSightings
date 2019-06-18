package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.ufo.model.Sighting;
import it.polito.tdp.ufo.model.State;

public class SightingsDAO {

	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Sighting> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
							res.getString("city"), res.getString("state"), res.getString("country"),
							res.getString("shape"), res.getInt("duration"), res.getString("duration_hm"),
							res.getString("comments"), res.getDate("date_posted").toLocalDate(),
							res.getDouble("latitude"), res.getDouble("longitude")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<State> getStatesUS() {
		String sql = "SELECT DISTINCT state FROM sighting WHERE country='us'";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<State> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new State(res.getString("state"), new LinkedList<Sighting>()));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Sighting> getSightingsUS() {
		String sql = "SELECT * FROM sighting WHERE country='us'";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Sighting> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
							res.getString("city"), res.getString("state"), res.getString("country"),
							res.getString("shape"), res.getInt("duration"), res.getString("duration_hm"),
							res.getString("comments"), res.getDate("date_posted").toLocalDate(),
							res.getDouble("latitude"), res.getDouble("longitude")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public boolean esisteArco(String s1, String s2, int anno) {
		String sql = "select count(*) as cnt " + 
				"from Sighting s1,Sighting s2 " + 
				"where Year(s1.datetime) = Year(s2.datetime) " + 
				"	and Year(s1.datetime) = ? and " + 
				"	s1.state = ? and s2.state = ? and " + 
				"	s1.country = \"us\" and s2.country = \"us\" " + 
				"	and s2.datetime > s1.datetime";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setString(2, s1);
			st.setString(3, s2);
			ResultSet res = st.executeQuery();

			if (res.next()) {
				if (res.getInt("cnt") > 0) {
					conn.close();
					return true;
				} else {
					conn.close();
					return false;
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
