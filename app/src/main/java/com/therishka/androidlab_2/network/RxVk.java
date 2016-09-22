package com.therishka.androidlab_2.network;

import android.support.annotation.NonNull;

import com.therishka.androidlab_2.models.VkDialog;
import com.therishka.androidlab_2.models.VkDialogResponse;
import com.therishka.androidlab_2.models.VkFriend;
import com.therishka.androidlab_2.models.VkMessage;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.List;

import rx.AsyncEmitter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

/**
 * @author Rishad Mustafaev
 */

public class RxVk {

    public void getUsers(final RxVkListener<VKList<VKApiUser>> listener) {
        Subscriber<VKList<VKApiUser>> usersSubscriber = Subscribers.create(new Action1<VKList<VKApiUser>>() {
            @Override
            public void call(VKList<VKApiUser> vkApiUsers) {
                listener.requestFinished(vkApiUsers);
            }
        });
        pGetUsers().subscribe(usersSubscriber);
    }

    public void getFriends(final RxVkListener<List<VkFriend>> listener) {
        Subscriber<List<VkFriend>> friendsSubscriber = Subscribers.create(new Action1<List<VkFriend>>() {
            @Override
            public void call(List<VkFriend> vkApiUsers) {
                listener.requestFinished(vkApiUsers);
            }
        });
        pGetFriends(VKParameters.from("order", "hints", "fields", "name, photo_50, online"))
                .subscribe(friendsSubscriber);
    }

    public void getDialogs(final RxVkListener<VkDialogResponse> listener) {
        Subscriber<VkDialogResponse> dialogsSubscriber = Subscribers.create(new Action1<VkDialogResponse>() {
            @Override
            public void call(VkDialogResponse vkDialogResponse) {
                listener.requestFinished(vkDialogResponse);
            }
        });
        pGetDialogs().subscribe(dialogsSubscriber);
    }

    private Observable<VKList<VKApiUser>> pGetUsers() {
        return Observable.fromEmitter(new Action1<AsyncEmitter<VKResponse>>() {
            @Override
            public void call(final AsyncEmitter<VKResponse> vkResponseAsyncEmitter) {
                VKApi.users().get().executeWithListener(new RxVkRequestListener(vkResponseAsyncEmitter));
            }
        }, AsyncEmitter.BackpressureMode.LATEST)
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // do nothing
                    }
                })
                .flatMap(new Func1<VKResponse, Observable<VKList<VKApiUser>>>() {
                    @Override
                    public Observable<VKList<VKApiUser>> call(VKResponse vkResponse) {
                        Object parsedModel = vkResponse.parsedModel;
                        if (parsedModel != null && parsedModel instanceof VKList) {
                            VKList list = ((VKList) parsedModel);
                            if (checkCast(VKApiUser.class, list)) {
                                //noinspection unchecked
                                return Observable.just(((VKList<VKApiUser>) parsedModel));
                            }
                        }
                        return Observable.empty();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<VkFriend>> pGetFriends(final VKParameters params) {
        return Observable.fromEmitter(new Action1<AsyncEmitter<VKResponse>>() {
            @Override
            public void call(AsyncEmitter<VKResponse> vkResponseAsyncEmitter) {
                VKApi.friends().get(params)
                        .executeWithListener(new RxVkRequestListener(vkResponseAsyncEmitter));
            }
        }, AsyncEmitter.BackpressureMode.LATEST)
                .flatMap(new Func1<VKResponse, Observable<List<VkFriend>>>() {
                    @Override
                    public Observable<List<VkFriend>> call(VKResponse vkResponse) {
                        Object parsedModel = vkResponse.parsedModel;
                        if (parsedModel != null && parsedModel instanceof VKList) {
                            VKList list = ((VKList) parsedModel);
                            if (checkCast(VKApiUser.class, list)) {
                                //noinspection unchecked
                                VKList<VKApiUser> castedList = ((VKList<VKApiUser>) list);
                                List<VkFriend> friends = new ArrayList<VkFriend>();
                                for (VKApiUser user : castedList) {
                                    VkFriend friend = new VkFriend();
                                    friend.setName(user.first_name);
                                    friend.setSurname(user.last_name);
                                    friend.setOnline(user.online);
                                    friend.setSmallPhotoUrl(user.photo_50);
                                    friends.add(friend);
                                }
                                return Observable.just(friends);
                            }
                        }
                        return Observable.empty();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<VkDialogResponse> pGetDialogs() {
        return Observable.fromEmitter(new Action1<AsyncEmitter<VKResponse>>() {
            @Override
            public void call(AsyncEmitter<VKResponse> vkDialogAsyncEmitter) {
                VKApi.messages().getDialogs().executeWithListener(new RxVkRequestListener(vkDialogAsyncEmitter));
            }
        }, AsyncEmitter.BackpressureMode.LATEST)
                .flatMap(new Func1<VKResponse, Observable<VkDialogResponse>>() {
                    @Override
                    public Observable<VkDialogResponse> call(VKResponse vkResponse) {
                        Object parsedModel = vkResponse.parsedModel;
                        if (parsedModel != null && parsedModel instanceof VKApiGetMessagesResponse) {
                            VKApiGetMessagesResponse castedModel = ((VKApiGetMessagesResponse) parsedModel);
                            VkDialogResponse response = new VkDialogResponse();

                            List<VkDialog> dialogs = new ArrayList<VkDialog>();
                            for (VKApiMessage message : castedModel.items) {
                                VkDialog dialog = new VkDialog();
                                VkMessage vkMessage = new VkMessage();

                                vkMessage.setDate(message.date);
                                vkMessage.setId(message.id);
                                vkMessage.setMessageBody(message.body);
                                vkMessage.setRead(message.read_state);
                                vkMessage.setTitle(message.title);
                                vkMessage.setUser_id(message.user_id);

                                dialog.setVkMessage(vkMessage);
                            }

                            response.setCount(castedModel.count);
                            response.setDialogs(dialogs);

                            return Observable.just(response);
                        }

                        return Observable.empty();
                    }
                });
    }

    private boolean checkCast(Class classz, @NonNull VKList list) {
        return !list.isEmpty() && list.get(0) != null && (classz.isInstance(list.get(0)));
    }

    public interface RxVkListener<T> {
        void requestFinished(T requestResult);
    }
}
