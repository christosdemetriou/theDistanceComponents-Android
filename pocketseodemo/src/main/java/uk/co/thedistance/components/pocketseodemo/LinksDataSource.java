package uk.co.thedistance.components.pocketseodemo;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.thedistance.components.lists.model.ListContent;
import uk.co.thedistance.components.lists.interfaces.ListDataSource;
import uk.co.thedistance.components.pocketseodemo.model.DataRepository;
import uk.co.thedistance.components.pocketseodemo.model.MozScapeLink;
import uk.co.thedistance.components.pocketseodemo.mozscape.model.MSLinkFilter;

public class LinksDataSource implements ListDataSource<MozScapeLink> {

    DataRepository repository;
    String website;
    int page = 1;
    boolean shouldClear = true;
    boolean isComplete = false;

    public LinksDataSource(DataRepository repository, String website) {
        this.repository = repository;
        this.website = website;
    }

    @Override
    public boolean isListComplete() {
        return isComplete;
    }

    @Override
    public void reset() {
        page = 1;
        shouldClear = true;
        isComplete = false;
    }

    @Override
    public Observable<ListContent<MozScapeLink>> getData() {

        return repository.getLinkMetrics(website, page, new MSLinkFilter(), true)
                .map(new Func1<List<MozScapeLink>, ListContent<MozScapeLink>>() {
                    @Override
                    public ListContent<MozScapeLink> call(List<MozScapeLink> mozScapeLinks) {
                        ListContent<MozScapeLink> content = new ListContent<MozScapeLink>(mozScapeLinks, shouldClear);
                        shouldClear = false;
                        isComplete = mozScapeLinks.size() > 25;
                        page++;
                        return content;
                    }
                });
    }
}
