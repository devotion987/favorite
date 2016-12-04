package com.devotion.blue.web.admin;

import com.devotion.blue.model.Attachment;
import com.devotion.blue.model.User;
import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.model.template.TemplateManager;
import com.devotion.blue.model.template.Thumbnail;
import com.devotion.blue.utils.AttachmentUtils;
import com.devotion.blue.utils.FileUtils;
import com.devotion.blue.utils.ImageUtils;
import com.devotion.blue.web.core.JBaseController;
import com.devotion.blue.web.core.interceptor.ActionCacheClearInterceptor;
import com.devotion.blue.web.router.RouterMapping;
import com.devotion.blue.web.router.RouterNotAllowConvert;
import com.jfinal.aop.Before;
import com.jfinal.core.JFinal;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import com.jfinal.upload.UploadFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import static com.jfinal.render.Render.getDevMode;

@RouterMapping(url = "/admin/tinymce/image")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _TinymceImageController extends JBaseController {
    private static final Log log = Log.getLog(_TinymceImageController.class);
	/**
	 * 下载远程文件
	 */
	public void proxy() {
		String url = getPara("url");
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setInstanceFollowRedirects(true);
			conn.setUseCaches(true);
			InputStream is = conn.getInputStream();

			setHeader("Content-Type", conn.getContentType());

			render(new StreamRender(is));
		} catch (Exception e) {
		}
	}

	/**
	 * 上传文件
	 */
	public void upload() {
		UploadFile uploadFile = getFile();
		String newPath = AttachmentUtils.moveFile(uploadFile);
		User user = getLoginedUser();

		Attachment attachment = new Attachment();
		attachment.setUserId(user.getId());
		attachment.setCreated(new Date());
		attachment.setTitle(uploadFile.getOriginalFileName());
		attachment.setPath(newPath.replace("\\", "/"));
		attachment.setSuffix(FileUtils.getSuffix(uploadFile.getFileName()));
		attachment.setMimeType(uploadFile.getContentType());

		attachment.save();
		
		processImage(newPath);

		renderJson("location", JFinal.me().getContextPath() + newPath);
	}
	
	
	private void processImage(String newPath) {
		if (!AttachmentUtils.isImage(newPath))
			return;
		
		if(".gif".equalsIgnoreCase(FileUtils.getSuffix(newPath))){
			// 过滤 .gif 图片
			return;
		}

		try {
			// 由于内存不够等原因可能会出未知问题
			processThumbnail(newPath);
		} catch (Throwable e) {
			log.error("processThumbnail error", e);
		}
		try {
			// 由于内存不够等原因可能会出未知问题
			processWatermark(newPath);
		} catch (Throwable e) {
			log.error("processWatermark error", e);
		}
	}

	private void processThumbnail(String newPath) {
		List<Thumbnail> tbs = TemplateManager.me().currentTemplate().getThumbnails();
		if (tbs != null && tbs.size() > 0) {
			for (Thumbnail tb : tbs) {
				try {
					String newSrc = ImageUtils.scale(PathKit.getWebRootPath() + newPath, tb.getWidth(), tb.getHeight());
					processWatermark(FileUtils.removeRootPath(newSrc));
				} catch (IOException e) {
					log.error("processWatermark error", e);
				}
			}
		}
	}

	public void processWatermark(String newPath) {
		Boolean watermark_enable = OptionQuery.me().findValueAsBool("watermark_enable");
		if (watermark_enable != null && watermark_enable) {

			int position = OptionQuery.me().findValueAsInteger("watermark_position");
			String watermarkImg = OptionQuery.me().findValue("watermark_image");
			String srcImageFile = newPath;

			Float transparency = OptionQuery.me().findValueAsFloat("watermark_transparency");
			if (transparency == null || transparency < 0 || transparency > 1) {
				transparency = 1f;
			}

			srcImageFile = PathKit.getWebRootPath() + srcImageFile;

			File watermarkFile = new File(PathKit.getWebRootPath(), watermarkImg);
			if (!watermarkFile.exists()) {
				return;
			}

			ImageUtils.pressImage(watermarkFile.getAbsolutePath(), srcImageFile, srcImageFile, position, transparency);
		}
	}


	public class StreamRender extends Render {
		final InputStream stream;

		public StreamRender(InputStream is) {
			this.stream = is;
		}

		@Override
		public void render() {
			InputStream inputStream = null;
			OutputStream outputStream = null;
			try {
				inputStream = new BufferedInputStream(stream);
				outputStream = response.getOutputStream();
				byte[] buffer = new byte[1024];
				for (int len = -1; (len = inputStream.read(buffer)) != -1;) {
					outputStream.write(buffer, 0, len);
				}
				outputStream.flush();
			} catch (IOException e) {
				if (getDevMode()) {
					throw new RenderException(e);
				}
			} catch (Exception e) {
				throw new RenderException(e);
			} finally {
				close(inputStream, outputStream);
			}
		}

		private void close(InputStream inputStream, OutputStream outputStream) {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					LogKit.error(e.getMessage(), e);
				}
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (IOException e) {
					LogKit.error(e.getMessage(), e);
				}
		}

	}

}