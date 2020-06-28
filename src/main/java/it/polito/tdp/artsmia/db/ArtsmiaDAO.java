package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artista;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {

		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"),
						res.getString("continent"), res.getString("country"), res.getInt("curator_approved"),
						res.getString("dated"), res.getString("department"), res.getString("medium"),
						res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"),
						res.getString("rights_type"), res.getString("role"), res.getString("room"),
						res.getString("style"), res.getString("title"));

				result.add(artObj);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Exhibition> listExhibitions() {

		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"),
						res.getString("exhibition_title"), res.getInt("begin"), res.getInt("end"));

				result.add(exObj);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getRuoli() {

		String sql = "Select Distinct role " + "from authorship ORDER BY role";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("role"));
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Artista> getNodi(String genere) {

		String sql = "Select Distinct a.artist_id as id, a.name as nome " + 
				"from authorship as au, artists as a " + 
				"where au.role=? and au.`artist_id`=a.artist_id  ";
		List<Artista> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Artista a = new Artista(res.getInt("id"), res.getString("nome"));
				result.add(a);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Adiacenza> getArchi(String genere) {

		String sql = "Select a1.`artist_id` as id1,ar1.`name` as nome1, " + 
				"       a2.`artist_id` as id2, ar2.name as nome2, " + 
				"       count(Distinct(eo1.`exhibition_id`)) as peso " + 
				"from authorship as a1, authorship as a2, exhibition_objects as eo1, exhibition_objects as eo2, artists as ar1, artists as ar2 " + 
				"where a1.role=? and a2.role=? " + 
				"and a1.artist_id<>a2.artist_id  " + 
				"and eo1.object_id=a1.object_id and eo2.object_id=a2.object_id " + 
				"and eo1.`exhibition_id`=eo2.`exhibition_id`\n" + 
				"and a1.`artist_id`=ar1.`artist_id` and a2.`artist_id`=ar2.`artist_id` " + 
				"Group by a1.`artist_id`, a2.`artist_id`  ";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			st.setString(2, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Artista a1 = new Artista(res.getInt("id1"), res.getString("nome1"));
				Artista a2 = new Artista(res.getInt("id2"), res.getString("nome2"));
				result.add(new Adiacenza(a1, a2, res.getInt("peso")));
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
