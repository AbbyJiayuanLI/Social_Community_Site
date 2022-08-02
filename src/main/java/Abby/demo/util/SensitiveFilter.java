package Abby.demo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SensitiveFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class); 
	private static final String REPLACE_WORD = "*";
	
	private TrieNode rootNode = new TrieNode();
	
	// 构造trie
	@PostConstruct
	public void init() {
		try(InputStream iStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));	
		){
			String keyword;
			while((keyword=reader.readLine())!=null) {
				this.addKeyword(keyword);
			}
		}catch (IOException e) {
			logger.error("加载敏感词文件失败"+e.getMessage());
		}
	}
	
	
	/*
	 *  过滤敏感词
	 *  @param: String text
	 *  @return: String textAfterFilter
	 */
	public String filter(String text) {
		TrieNode cur = new TrieNode();
		int start = 0;
		int end = 0;
		StringBuilder sb = new StringBuilder();
		
		// 3.1 逻辑！！！！！！
		// 特殊符号的处理 		
		while (start<text.length()) {
			if (end<text.length()) {
				// end没越界
				char c = text.charAt(end);
				
				// 1. 符号
				if (isSymbol(c)) {
					if (cur==rootNode) {
						sb.append(c);
						start++;
					}
					end++;
					continue;
				}
				
				// 2. 检查
				cur = cur.getSubNode(c);
				if (cur!=null && cur.isEnd()) {
					sb.append(REPLACE_WORD);
					start = ++end;
					cur = rootNode;
					continue;
				} else if (cur!=null) {
					end++;
					continue;
				}
			} 
			sb.append(text.charAt(start));
			cur = rootNode;
			end = ++start;
						
		}
		return sb.toString();
		
		
	}
	



	private void addKeyword(String keyword) {
		TrieNode cur = rootNode;
		for (int i=0;i<keyword.length();i++) {
			char c = keyword.charAt(i);
			TrieNode subNode = cur.getSubNode(c);
			if (subNode==null) {
				subNode = new TrieNode();
				cur.addSubNode(c, subNode);  
			}
			cur = subNode;
			if (i==keyword.length()-1) {
				cur.setEnd(true);
			}
		}
		
	}
	
	
	public boolean isSymbol(Character c) {
		return !CharUtils.isAsciiAlphanumeric(c) && (c<0x2E80 || c>0x9FFF);
	}
	
	
	
	
	private class TrieNode {
		private boolean isEnd = false;
		// <字符,节点>
		private Map<Character, TrieNode> subNodesMap = new HashMap<Character, SensitiveFilter.TrieNode>();

		public void addSubNode(Character c, TrieNode node) {
			subNodesMap.put(c,node);
		}
		
		public TrieNode getSubNode(Character c) {
			return subNodesMap.get(c);
		}
		
		public boolean isEnd() {
			return isEnd;
		}

		public void setEnd(boolean isEnd) {
			this.isEnd = isEnd;
		}
	}
	
	

}
