package com.java.myproject.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.java.myproject.dto.ContentDTO;
import com.java.myproject.util.DatabaseUtil;

@Repository
public class ContentDAOImpl implements ContentDAO {
	
	@Override
	public int write (String userID, String topicName, String contentName, String time, String board) throws Exception {
		String sql = "INSERT INTO "+ board + " VALUES (NULL, ?, ?, ?, ?, 0, 0)";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID.replaceAll("<", "&alt;").replaceAll(">", "&gt;").replaceAll("/r/n", "<br>"));
			pstmt.setString(2, topicName.replaceAll("<", "&alt;").replaceAll(">", "&gt;").replaceAll("/r/n", "<br>"));
			pstmt.setString(3, contentName.replaceAll("<", "&alt;").replaceAll(">", "&gt;").replaceAll("/r/n", "<br>"));
			pstmt.setString(4, time.replaceAll("<", "&alt;").replaceAll(">", "&gt;").replaceAll("/r/n", "<br>"));
			
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn != null) conn.close(); } catch(Exception e) { e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close(); } catch(Exception e) { e.printStackTrace(); }
			try { if(rs != null) rs.close(); } catch(Exception e) { e.printStackTrace(); }
		}
		
		return -1;
	}
	
	@Override
	public ArrayList<ContentDTO> getList (String boardType, String searchType, String search, int pageNumber) throws Exception {
		ArrayList<ContentDTO> contentList = null;
		String sql = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			sql = "SELECT * FROM " + boardType + " ORDER BY writeID DESC LIMIT " + (pageNumber-1) * 10  + "," + pageNumber * 10; 
			
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			contentList = new ArrayList<ContentDTO>();
			
			while(rs.next()) {
				ContentDTO content = new ContentDTO(
						rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						boardType,
						rs.getInt(6),
						rs.getInt(7)
				);
				contentList.add(content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contentList;
	}
	
	@Override
	public int targetPage(int countindex,String boardType) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(writeID) FROM " + boardType + " WHERE writeID < ?";
		
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, countindex);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int tmp = 0;
				return rs.getInt(1);
			} 
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.clearBatch();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		// ������ �߻��Ͽ��� ��
		return 0;
	}
	
	@Override
	public ContentDTO getContent(String boardType, int idx) throws Exception {
		ContentDTO cntdto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM " + boardType + " WHERE writeID = ?";
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idx);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String tmpstr = rs.getString(4);
				tmpstr = tmpstr.replaceAll("��","'"); // ġȯ�� ��������ǥ ���� ��������ǥ�� ȯ��ó��
				tmpstr = tmpstr.replaceAll("\r\n","<br>"); // �ٹٲ�ó��
				tmpstr = tmpstr.replaceAll("\u0020","&nbsp;"); // �����̽��ٷ� ��� ����ó��
				
				cntdto = new ContentDTO(
						idx,
						rs.getString(2),
						rs.getString(3),
						tmpstr,
						rs.getString(5),
						boardType,
						rs.getInt(6),
						rs.getInt(7)
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn != null) conn.close(); } catch(Exception e) { e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close(); } catch(Exception e) { e.printStackTrace(); }
			try { if(rs != null) rs.close(); } catch(Exception e) { e.printStackTrace(); }
		}
		
		return cntdto;
	}
	
	@Override
	public boolean Delete(String boardType, int writeID) throws Exception {
		String content_dsql = "DELETE FROM " + boardType + " WHERE writeID = ?";
		String comment_dsql = "DELETE FROM " + boardType + "_Comment WHERE writeID = ?";
		String like_dsql = "DELETE FROM likeDB WHERE boardType = ? AND writeID = ?";
		
		Connection conn = null;;
		PreparedStatement pstmt = null;
		
		try {
			conn = DatabaseUtil.getConnection();
			// �Խñ��� ����� �����ͺ��̽� ���ǹ�
			pstmt = conn.prepareStatement(content_dsql);
			pstmt.setInt(1, writeID);
			pstmt.executeUpdate();
			
			// �Խñ��� ����� ����� �����ͺ��̽� ���ǹ�
			pstmt = conn.prepareStatement(comment_dsql);
			pstmt.setInt(1, writeID);
			pstmt.executeUpdate();
			
			// �Խñ��� ���ƿ並 ����� �����ͺ��̽� ���ǹ�
			pstmt = conn.prepareStatement(like_dsql);
			pstmt.setString(1, boardType);
			pstmt.setInt(2, writeID);
			pstmt.executeUpdate();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn != null) conn.close(); } catch(Exception e) { e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close(); } catch(Exception e) { e.printStackTrace(); }
		}
		
		return false;
	}
	
	@Override
	public boolean SearchUp(String boardType, int writeID) throws Exception {
		String sql = "UPDATE " + boardType + " SET searchCount = searchCount + 1 WHERE writeID = ? ";
		Connection conn = null;;
		PreparedStatement pstmt = null;
		
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, writeID);
			pstmt.executeUpdate();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn != null) conn.close(); } catch(Exception e) { e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close(); } catch(Exception e) { e.printStackTrace(); }
		}
		
		return false;
	}
}
