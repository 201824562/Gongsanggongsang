import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gongsanggongsang.data.CommunityMarketPostModel
import com.example.gongsanggongsang.R
import com.example.gongsanggongsang.Third.CommunityPreviewRecyclerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_community_market.*
import java.time.LocalDateTime

class MarketFragment : Fragment() {
    var database = FirebaseFirestore.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_community_market, container, false)
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        community_market_write_button.setOnClickListener{
            findNavController().navigate(R.id.action_communityMarket_to_communityMarketWrite)
        }

    }
    fun initRecyclerView(){
        var testlist = arrayListOf<CommunityMarketPostModel>()
        var mCommunityRecyclerAdapter: CommunityPreviewRecyclerAdapter = CommunityPreviewRecyclerAdapter(testlist).apply {
            listener = object : CommunityPreviewRecyclerAdapter.OnCommunityMarketItemClickListener{
                override fun onMarketItemClick(position: Int) {
                    var test = getItem(position).title.toString() + getItem(position).date.toString()
                    var bundle = bundleOf(
                            "key" to test
                    )
                    findNavController().navigate(R.id.communityMarketPost, bundle)
                }
            }
        }

        community_market_preview_recycler_view.run{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mCommunityRecyclerAdapter
        }
        database.collection("COMMUNITY_Market")
                .get()
                .addOnSuccessListener { result ->
                    testlist.clear()
                    for (document in result){
                        val item = CommunityMarketPostModel(0.toLong(),"주용가리", document["title"] as String, document["contents"] as String, document["date"] as String)
                        testlist.add(item)
                    }
                    mCommunityRecyclerAdapter.notifyDataSetChanged()
                }
    }
}