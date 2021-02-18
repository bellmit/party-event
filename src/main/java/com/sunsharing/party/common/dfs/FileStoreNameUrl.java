/**
 * @ProjectName: air-lesson
 * @Copyright: 2016 Sunsharing Technology Co., Ltd. All Right Reserved.
 * @address: http://www.sunsharing.com.cn
 * @date: 2017年8月14日 下午2:32:48
 * @Description: 本内容仅限于厦门畅享信息技术有限公司内部使用，禁止转发.
 */
package com.sunsharing.party.common.dfs;

/**
 * <p>
 * </p>
 * @author lixinqiao 2017年8月14日 下午2:32:48
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年8月14日
 * @modify by reason:{方法名}:{原因}
 */
public class FileStoreNameUrl {
	
	public static final String PREFIX_IMG = "/static/images";// 提交图片
	public static final String PREFIX_VEDIO = "/static/video";// 提交视频
	public static final String PREFIX_AUDIO = "/static/audio";// 提交音频
	public static final String DRAFT_IMG = "/draft/images";// 草稿图片
	public static final String DRAFT_VEDIO = "/draft/video";// 草稿视频
	public static final String DRAFT_AUDIO = "/draft/audio";// 草稿音频
	public static final String DRAFT_TYPE = "/draft/";// 表示草稿存储的路径类型
	public static final String PREFIX_TYPE = "/static/";// 表示提交存储的路径类型
	public static final String DEFAULT_URL = "caogao";// 保存的文件默认存储在caogao文件夹中
	public static final String FOREVER = "forever";// 表示永久文件
	public static final String TEMP = "temp";// 表示临时文件
	public static final String TEMPORARY = "temporary";// 表示暂存文件
	public static final String DRAFT = "draft";// 表示草稿文件
	public static final String isWx = "1";// 表示从微信二维码扫描过来
	public static final String dataFromPc = "1";// 表示课程是从pc端发布
}
