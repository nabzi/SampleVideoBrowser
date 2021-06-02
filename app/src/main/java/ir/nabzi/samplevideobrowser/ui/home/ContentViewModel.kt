package ir.nabzi.samplevideobrowser.ui.home

import androidx.lifecycle.*
import ir.nabzi.samplevideobrowser.data.repository.ContentRepository
import ir.nabzi.samplevideobrowser.model.Content
import ir.nabzi.samplevideobrowser.model.Resource
import kotlinx.coroutines.launch

class ContentViewModel(private val ContentRepository  : ContentRepository
                     ) : ViewModel(){

    val  selectedContentId = MutableLiveData<String>()
    var contentList : LiveData<Resource<List<Content>>>? = null //= currentLocation.switchMap {
//            page = 1
//            ContentRepository.getContentsNearLocation(it.latitude, it.longitude, viewModelScope, 1 ,
//                lastLocation.distanceTo(it) > MIN_LOCATION_CHANGE)
//                    .asLiveData()
//    }


    val Content = selectedContentId.map { _id ->
        contentList?.value?.data?.firstOrNull { it.id == _id }
    }

}
