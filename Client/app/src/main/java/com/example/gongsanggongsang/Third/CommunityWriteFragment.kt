import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gongsanggongsang.R
import com.example.gongsanggongsang.data.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_community_write.*
import java.time.LocalDateTime

class CommunityWriteFragment : Fragment() {
    val viewModel: PostDataViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_community_write, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var collection_name = arguments?.getString("collection_name").toString()
        write_register_button.setOnClickListener{
            val post = PostDataClass(collection_name
                ,"주용가"
                ,write_title.text.toString()
                ,write_contents.text.toString()
                , LocalDateTime.now().toString()
                , ArrayList<PostCommentDataClass>())

            viewModel.insertPostData(post)

            var document_name = LocalDateTime.now().toString() + write_title.text.toString()
            var bundle = bundleOf(
                "collection_name" to collection_name,
                "document_name" to document_name
            )
            //findNavController().navigate(R.id.communityPost, bundle)
        }
        //val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_third) as NavHostFragment? ?: return
        //findNavController().navigate(R.id.third)
    }
}