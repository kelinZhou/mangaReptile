package com.neuifo.mangareptile.domain.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.neuifo.mangareptile.R;

import java.io.File;
import java.util.UUID;

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(transFerUrl(url)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(placeholder)
                .error(error)
        ).into(imageView);
    }

    public static void display(ImageView imageView, String url, int res) {
        Glide.with(imageView.getContext())
                .load(transFerUrl(url))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .placeholder(res)
                        .error(res))
                .into(imageView);
    }

    public static void display(ImageView imageView, String url, int loadingRes, int errorRes) {
        Glide.with(imageView.getContext())
                .load(transFerUrl(url))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(loadingRes)
                        .error(errorRes))
                .into(imageView);
    }

    public static void display(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(transFerUrl(url))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                ).into(imageView);
    }

    public static void display(ImageView imageView, String url, RequestListener requestListener) {
        Glide.with(imageView.getContext()).load(transFerUrl(url))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                )
                .listener(requestListener)
                .into(imageView);
    }


    public static void displayAvatar(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(transFerUrl(url))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .error(R.drawable.avatar_default)
                        .placeholder(R.drawable.avatar_default)
                        .centerCrop()
                )
                .into(imageView);
    }

    public static void display(ImageView imageView, byte[] url, RequestListener requestListener) {
        Glide.with(imageView.getContext()).load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                )
                .listener(requestListener)
                .into(imageView);
    }

    public static void displayRound(ImageView imageView, int res) {
        Glide.with(imageView.getContext()).load(res)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .transforms(new CircleCrop()))
                .into(imageView);
    }

    public static void displayRound(ImageView imageView, String url, int res) {
        Glide.with(imageView.getContext()).load(transFerUrl(url))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(res)
                        .error(res)
                        .transforms(new CircleCrop()))
                .into(imageView);
    }

    public static void displayRoundCorners(ImageView imageView, String url, int cornerRedis) {
        Glide.with(imageView.getContext()).load(transFerUrl(url))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .transforms(new RoundedCorners(cornerRedis)))
                .into(imageView);
    }

    private static String transFerUrl(String url) {
        return url;
    }

    public static void displayRound(ImageView imageView, String url, int res, Transformation transformation) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(res)
                        .error(res)
                        .transforms(transformation))
                .into(imageView);
    }

    public static void displayRoundAvatar(ImageView imageView, String url, int res) {
        Glide.with(imageView.getContext()).load(transFerUrl(url))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(res)
                        .error(res)
                        .transforms(new CircleCrop()))
                .into(imageView);
    }

    public static void displayRoundAvatar(ImageView imageView, String url, int res, RequestListener requestListener) {
        Glide.with(imageView.getContext()).load(transFerUrl(url))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(res)
                        .error(res)
                        .transforms(new CircleCrop()))
                .listener(requestListener)
                .into(imageView);
    }

    public static void displayCommonRoundAvatar(ImageView imageView, String url) {
        displayCommonRoundAvatar(imageView, url, 20);
    }

    public static void displayCommonRoundAvatar(ImageView imageView, String url, int cornerRedis) {
        displayRoundAvatar(imageView, url, R.drawable.avatar_default, cornerRedis);
    }

    public static void displayRoundAvatar(ImageView imageView, String url, int res, int cornerRedis) {
        Glide.with(imageView.getContext()).load(transFerUrl(url))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(res)
                        .error(res)
                        .transforms(new RoundedCorners(cornerRedis)))
                .into(imageView);
    }

    public static void displayRoundAvatar(ImageView imageView, String url, int res, RequestListener requestListener, int cornerRedis) {
        Glide.with(imageView.getContext()).load(transFerUrl(url))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(res)
                        .error(res)
                        .transforms(new RoundedCorners(cornerRedis)))
                .listener(requestListener)
                .into(imageView);
    }

    public static void displayRoundAvatar(ImageView imageView, File file, int res, RequestListener requestListener) {
        Glide.with(imageView.getContext()).load(file)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(res)
                        .error(res)
                        .transforms(new CircleCrop()))
                .listener(requestListener)
                .into(imageView);
    }

    public static void displayRoundAvatar(ImageView imageView, File file, int res, RequestListener requestListener, int cornerRedis) {
        Glide.with(imageView.getContext()).load(file)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(res)
                        .error(res)
                        .transforms(new RoundedCorners(cornerRedis)))
                .listener(requestListener)
                .into(imageView);
    }

    public static void displayWithPlaceHolder(Context context, ImageView imageView, String url, int resId) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(resId)
                        .error(resId)
                )
//                .error(R.drawable.ic_empty_picture)
                .into(imageView);
    }

    public static void displayWithLoading(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .transforms(new CenterCrop()))
                .into(imageView);
    }


    public static void display(Context context, ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .transforms(new CenterCrop()))
                .into(imageView);
    }


    public static void displayRoundNoCache(Context context, ImageView imageView, File url, int corner) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .transforms(new RoundedCorners(corner))
                        .signature(new ObjectKey(UUID.randomUUID())))
                .into(imageView);
    }

    public static void displaySmallPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .transforms(new CenterCrop()))
                .thumbnail(0.5f)
                .into(imageView);
    }

    public static void displayBigPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .transforms(new CenterCrop()))
//                .placeholder(R.drawable.ic_image_loading)
//                .error(R.drawable.ic_empty_picture)
                .into(imageView);
    }

    public static void display(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .centerCrop()
                )
                .into(imageView);
    }
}
