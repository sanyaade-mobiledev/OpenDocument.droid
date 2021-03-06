package at.tomtasche.reader.ui.widget;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import at.tomtasche.reader.R;
import at.tomtasche.reader.background.Document;
import at.tomtasche.reader.background.Document.Part;

public class DocumentFragment extends Fragment {

	public static final String FRAGMENT_TAG = "document_fragment";

	private static final String EXTRA_SCROLL_POSITION = "scroll_position";
	private static final String EXTRA_CURRENT_PAGE = "current_page";
	private static final String EXTRA_DOCUMENT = "document";

	private DocumentView documentView;
	private Document document;
	private int currentIndex;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			document = savedInstanceState.getParcelable(EXTRA_DOCUMENT);
			currentIndex = savedInstanceState.getInt(EXTRA_CURRENT_PAGE);

			documentView = new DocumentView(getActivity(),
					savedInstanceState.getInt(EXTRA_SCROLL_POSITION));
			if (document != null) {
				if (currentIndex < document.getPages().size()) {
					documentView.loadUrl(document.getPageAt(currentIndex)
							.getUrl());
				} else if (document.getPages().size() > 0) {
					document.getPageAt(0);
				}
			}
		} else {
			document = new Document();

			documentView = new DocumentView(getActivity());
			documentView.loadData(
					getActivity().getString(R.string.message_get_started),
					"text/plain", DocumentView.ENCODING);
		}

		return documentView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(EXTRA_SCROLL_POSITION, documentView.getScrollY());
		outState.putInt(EXTRA_CURRENT_PAGE, currentIndex);
		outState.putParcelable(EXTRA_DOCUMENT, document);
	}

	private void loadData(String url) {
		documentView.loadUrl(url);
	}

	public void loadDocument(Document document) {
		this.document = document;

		currentIndex = 0;

		if (document.getPages().size() == 0)
			return;

		Part firstPage = document.getPageAt(currentIndex);
		loadData(firstPage.getUrl());
	}

	@SuppressWarnings("deprecation")
	public void searchDocument(String query) {
		documentView.findAll(query);
	}

	public boolean nextPage() {
		return goToPage(currentIndex + 1);
	}

	public boolean previousPage() {
		return goToPage(currentIndex - 1);
	}

	public boolean goToPage(int page) {
		if (page < 0 || page >= document.getPages().size())
			return false;

		currentIndex = page;

		loadData(document.getPageAt(currentIndex).getUrl());

		return true;
	}

	public List<Part> getPages() {
		return document.getPages();
	}

	public DocumentView getDocumentView() {
		return documentView;
	}
}
