package ir.nabzi.samplevideobrowser.ui.home

import androidx.lifecycle.*
import ir.nabzi.samplevideobrowser.data.repository.ContentRepository
import ir.nabzi.samplevideobrowser.model.Content
import ir.nabzi.samplevideobrowser.model.Resource
import kotlinx.coroutines.launch

class ContentViewModel(private val ContentRepository  : ContentRepository
                     ) : ViewModel(){

    val  selectedContentId = MutableLiveData<String>()
    val searchPhrase = MutableLiveData<String>("")
    var contentList : LiveData<Resource<List<Content>>?> = searchPhrase.switchMap {
            ContentRepository.getContents(it, viewModelScope, 1 , true)
                    .asLiveData()
    }

    val selectedContent = selectedContentId.map { _id ->
        contentList?.value?.data?.firstOrNull { it.id == _id }
    }

}
